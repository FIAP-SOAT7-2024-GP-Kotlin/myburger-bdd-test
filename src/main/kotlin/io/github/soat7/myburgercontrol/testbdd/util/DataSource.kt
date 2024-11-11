package io.github.soat7.myburgercontrol.testbdd.util

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

object DataSource {

    private val config = Configuration
    private val hikariConfig = HikariConfig()
    private val dataSource: HikariDataSource

    init {
        hikariConfig.driverClassName = config["myburger.datasource.driver-class-name"]
        hikariConfig.jdbcUrl = config["myburger.datasource.url"]
        hikariConfig.username = config["myburger.datasource.username"]
        hikariConfig.password = config["myburger.datasource.password"]
        hikariConfig.maximumPoolSize = config["myburger.datasource.maxPoolSize"]?.toInt() ?: 10

        dataSource = HikariDataSource(hikariConfig)
    }

    fun connection() = dataSource.connection
}
