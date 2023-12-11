package com.example.submisionintermediate.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.submisionintermediate.data.database.databaseStory
import com.example.submisionintermediate.data.database.remoteKey
import com.example.submisionintermediate.data.database.storyItem
import com.example.submisionintermediate.data.retrofit.ApiService

@OptIn(ExperimentalPagingApi::class)
class StoryPagingSource(
    private val database: databaseStory,
    private val apiService: ApiService,
    token: String
) : RemoteMediator<Int, storyItem>() {
    var token: String? = token


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, storyItem>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }
        try {
            val data: List<storyItem>
            val response = apiService.getStoryAll("bearer $token", 0, page, state.config.pageSize)
            data = response.listStory
            val endOfPagingReached = data.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.getKey().deleteRemote()
                    with(database) { getListStoryDao().deleteAll() }
                }
                val prevKey = if (page == 1)
                    null else page - 1
                val nextKey = if (endOfPagingReached) null else page + 1
                val keys = data.map {
                    remoteKey(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.getKey().insertAll(keys)
                database.getListStoryDao().insertStory(data)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPagingReached)

        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, storyItem>): remoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.getKey().getRemote(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, storyItem>): remoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.getKey().getRemote(data.id)
        }
    }
    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, storyItem>): remoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.getKey().getRemote(id)
            }
        }
    }
    private companion object {
        const val PAGE_INDEX = 1
    }
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }
}
