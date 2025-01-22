package springbootkotlin.toy.user.login.repository.custom

import springbootkotlin.toy.entity.User

interface UserRepositoryCustom {
    fun findByUserIdAndPassword(userId: String, password: String): User?
}