package com.example.backend.graphql

open class SearchResult<T> (
    _totalCount: Int,
    _totalTime: Long,
    _pageCount: Int,
    _pageNum: Int,
    _fetchSize: Int,
    _hasNextPage: Boolean,
    private val objects: List<T?>
) : Result(_totalCount, _totalTime, _pageCount, _pageNum, _fetchSize, _hasNextPage) {

    fun isEmpty(): Boolean {
        return objects.isEmpty()
    }
}