package com.example.android.codelabs.paging.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.android.codelabs.paging.api.GithubService
import com.example.android.codelabs.paging.model.Repo
import kotlinx.coroutines.flow.Flow

const val NETWORK_PAGE_SIZE = 30

class GithubRepository(
    private val service: GithubService
) {

    fun getSearchResultStream(query: String): Flow<PagingData<Repo>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GithubPagingSource(service, query) }
        ).flow
    }

}
