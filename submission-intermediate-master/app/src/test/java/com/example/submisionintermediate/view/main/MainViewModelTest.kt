package com.example.submisionintermediate.view.main
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.submisionintermediate.DataDummy
import com.example.submisionintermediate.DataDummy.generateDummyStoryItems
import com.example.submisionintermediate.MainDispatcherRule
import com.example.submisionintermediate.adapter.StoryAdapter
import com.example.submisionintermediate.data.UserRepository
import com.example.submisionintermediate.data.database.storyItem
import com.example.submisionintermediate.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule=InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRules=MainDispatcherRule()

    @Mock
    private lateinit var mainViewModel:MainViewModel

    @Before
    fun setup(){
        mainViewModel=Mockito.mock(MainViewModel::class.java)
    }
    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest{
        val dummyStory=generateDummyStoryItems()
        val data = StoryPagingSource.snapshot(dummyStory)
        val expectedStory=MutableLiveData<PagingData<storyItem>>()
        val token="this token"
        expectedStory.value=data
        `when`(mainViewModel.getPagingStories(token)).thenReturn(expectedStory)

        val actualStory:PagingData<storyItem> = mainViewModel.getPagingStories(token).getOrAwaitValue()

        val differ=AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size,differ.snapshot())
        Assert.assertEquals(dummyStory[0],differ.snapshot()[0]?.name)
    }
    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun `when Get Story Empty Should Return No Data`()= runTest {
        val dummyStory=generateDummyStoryItems()
        val data = StoryPagingSource.snapshot(dummyStory)
        val expectedStory=MutableLiveData<PagingData<storyItem>>()
        val token="this token"
        expectedStory.value=data
        `when`(mainViewModel.getPagingStories(token)).thenReturn(expectedStory)

        val actualStory:PagingData<storyItem> = mainViewModel.getPagingStories(token).getOrAwaitValue()

        val differ=AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertTrue(differ.snapshot().isEmpty())

    }
    class StoryPagingSource:PagingSource<Int,LiveData<List<storyItem>>>(){
        companion object{
            fun snapshot(item: List<storyItem>):PagingData<storyItem>{
                return PagingData.from(item)
            }
        }
        override fun getRefreshKey(state: PagingState<Int, LiveData<List<storyItem>>>): Int? {
            return 0
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<storyItem>>> {
            return LoadResult.Page(emptyList(),0,1)
        }

    }
    val noopListUpdateCallback=object :ListUpdateCallback{
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}

    }
}
