package springbootkotlin.toy.repository.custom

import springbootkotlin.toy.entity.User

interface UserRepositoryCustom {
    fun findSpecialUsers(name: String, email: String): List<User>
}