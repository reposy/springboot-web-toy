package springbootkotlin.toy.users.auth.repository

import org.springframework.data.jpa.repository.JpaRepository
import springbootkotlin.toy.entity.User
import springbootkotlin.toy.users.auth.repository.custom.UserRepositoryCustom

interface UserRepository : JpaRepository<User, Long>, UserRepositoryCustom