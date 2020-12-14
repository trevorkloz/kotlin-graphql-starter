package com.example.backend.graphql

abstract class Result (
        open val _totalCount: Int,
        val _totalTime: Long,
        val _pageCount: Int,
        val _pageNum: Int,
        val _fetchSize: Int,
        val _hasNextPage: Boolean
)