package springbootkotlin.toy.user.login.repository

import org.springframework.data.jpa.repository.JpaRepository
import springbootkotlin.toy.entity.User
import springbootkotlin.toy.user.login.repository.custom.UserRepositoryCustom

interface UserRepository : JpaRepository<User, Long>, UserRepositoryCustom