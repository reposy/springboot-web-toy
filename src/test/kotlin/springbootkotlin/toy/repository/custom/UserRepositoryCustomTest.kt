package springbootkotlin.toy.repository.custom

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import springbootkotlin.toy.users.auth.repository.UserRepository
import springbootkotlin.toy.users.auth.repository.custom.impl.UserRepositoryCustomImpl

@SpringBootTest
@Transactional
class UserRepositoryCustomTest {

    @Autowired
    private lateinit var userRepositoryCustom: UserRepositoryCustomImpl

    @Autowired
    private lateinit var userRepository: UserRepository // 초기 데이터 삽입용
}