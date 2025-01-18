package springbootkotlin.toy.repository

import org.springframework.data.jpa.repository.JpaRepository
import springbootkotlin.toy.entity.User
import springbootkotlin.toy.repository.custom.UserRepositoryCustom

interface UserRepository : JpaRepository<User, Long>, UserRepositoryCustom