package springbootkotlin.toy.filter.password_encrypt.wrapper

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import org.springframework.security.crypto.password.PasswordEncoder
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets
import jakarta.servlet.ServletInputStream

class PasswordEncryptingWrapper(
    request: HttpServletRequest,
    passwordEncoder: PasswordEncoder
) : HttpServletRequestWrapper(request) {

    private val requestBody: String = request.reader.readText()
    private val modifiedBody: String

    init {
        val mapper = jacksonObjectMapper()
        val bodyMap: MutableMap<String, Any> = mapper.readValue(requestBody)

        // password 포함 필드 모두 변경
        bodyMap.forEach { (key, value) ->
            if (key.contains("password", ignoreCase = true) && value is String) {
                bodyMap[key] = passwordEncoder.encode(value) // 비밀번호 암호화
            }
        }
        modifiedBody = mapper.writeValueAsString(bodyMap)
    }

    override fun getInputStream(): ServletInputStream {
        val byteArrayInputStream = ByteArrayInputStream(modifiedBody.toByteArray(StandardCharsets.UTF_8))
        return object : ServletInputStream() {
            override fun read(): Int = byteArrayInputStream.read()
            override fun isFinished(): Boolean = byteArrayInputStream.available() == 0
            override fun isReady(): Boolean = true
            override fun setReadListener(listener: jakarta.servlet.ReadListener?) {}
        }
    }
    override fun getReader(): BufferedReader {
        return modifiedBody.reader().buffered()
    }
}