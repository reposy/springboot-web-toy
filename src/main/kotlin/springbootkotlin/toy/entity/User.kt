package springbootkotlin.toy.entity
import jakarta.persistence.*

@Entity
@Table(name = "USERS")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val userId: String,
    val encryptedPassword: String,
    val name: String,
    val email: String,

    val role: String = "ROLE_USER_NORMAL",
    val authority: String = "AUTH_USER_NORMAL"
)