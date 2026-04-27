package mx.unison.inventario.utileria;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas de CryptoUtil")
class CryptoUtilTest {

    @Nested
    @DisplayName("Método md5()")
    class MetodoMD5 {
        
        @Test
        @DisplayName("Debe generar hash MD5 correcto para 'hello'")
        void debeGenerarHashCorrecto() {
            String input = "hello";
            String expected = "5d41402abc4b2a76b9719d911017c592";
            String result = CryptoUtil.md5(input);
            assertEquals(expected, result);
        }

        @Test
        @DisplayName("Debe generar hash MD5 correcto para 'password'")
        void debeGenerarHashPassword() {
            String input = "password";
            String expected = "5f4dcc3b5aa765d61d8327deb882cf99";
            String result = CryptoUtil.md5(input);
            assertEquals(expected, result);
        }

        @Test
        @DisplayName("Debe generar hash MD5 correcto para 'admin'")
        void debeGenerarHashAdmin() {
            String input = "admin";
            String expected = "21232f297a57a5a743894a0e4a801fc3";
            String result = CryptoUtil.md5(input);
            assertEquals(expected, result);
        }

        @Test
        @DisplayName("Debe generar hash MD5 correcto para '123456'")
        void debeGenerarHash123456() {
            String input = "123456";
            String expected = "e99a18c428cb38d5f260853678922e03";
            String result = CryptoUtil.md5(input);
            assertEquals(expected, result);
        }

        @Test
        @DisplayName("Debe generar hash MD5 en minúsculas")
        void debeGenerarHashEnMinusculas() {
            String result = CryptoUtil.md5("test");
            assertTrue(result.matches("[a-f0-9]{32}"), "El hash debe estar en minúsculas");
        }

        @Test
        @DisplayName("El hash debe tener exactamente 32 caracteres (MD5)")
        void debeHaber32Caracteres() {
            String result = CryptoUtil.md5("cualquier_entrada");
            assertEquals(32, result.length(), "El hash MD5 debe tener 32 caracteres");
        }

        @Test
        @DisplayName("Debe ser consistente: mismo input = mismo output")
        void debeSerConsistente() {
            String input = "consistencia";
            String hash1 = CryptoUtil.md5(input);
            String hash2 = CryptoUtil.md5(input);
            String hash3 = CryptoUtil.md5(input);
            
            assertEquals(hash1, hash2);
            assertEquals(hash2, hash3);
        }

        @Test
        @DisplayName("Entradas diferentes deben producir hashes diferentes")
        void entradasDiferentesDiferentesHashes() {
            String hash1 = CryptoUtil.md5("entrada1");
            String hash2 = CryptoUtil.md5("entrada2");
            assertNotEquals(hash1, hash2);
        }

        @Test
        @DisplayName("Debe manejar cadenas vacías")
        void debeManejarCadenasVacias() {
            String result = CryptoUtil.md5("");
            String expected = "d41d8cd98f00b204e9800998ecf8427e";
            assertEquals(expected, result);
        }

        @Test
        @DisplayName("Debe manejar cadenas con espacios")
        void debeManejarCadenasConEspacios() {
            String result = CryptoUtil.md5("   ");
            String expected = "db346d691d7ac3414ebb5d911c8d2b51";
            assertEquals(expected, result);
        }

        @Test
        @DisplayName("Debe manejar cadenas largas")
        void debeManejarCadenasLargas() {
            String input = "a".repeat(1000);
            String result = CryptoUtil.md5(input);
            assertEquals(32, result.length());
            assertTrue(result.matches("[a-f0-9]{32}"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"ñ", "é", "á", "中文", "🔒"})
        @DisplayName("Debe manejar caracteres especiales y Unicode")
        void debeManejarCaracteresEspeciales(String input) {
            String result = CryptoUtil.md5(input);
            assertNotNull(result);
            assertEquals(32, result.length());
        }

        @Test
        @DisplayName("Debe generar hash para números como string")
        void debeGenerarHashParaNumeros() {
            String result = CryptoUtil.md5("123");
            assertNotNull(result);
            assertEquals(32, result.length());
        }

        @Test
        @DisplayName("Debe lanzar IllegalArgumentException para entrada nula")
        void debeLanzarExceptionParaNula() {
            assertThrows(IllegalArgumentException.class, () -> CryptoUtil.md5(null));
        }

        @Test
        @DisplayName("El mensaje de excepción debe ser apropiado")
        void mensajeExceptionApropiado() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> CryptoUtil.md5(null)
            );
            assertTrue(exception.getMessage().contains("null"));
        }
    }

    @Nested
    @DisplayName("Método verificar()")
    class MetodoVerificar {
        
        @Test
        @DisplayName("Debe verificar correctamente contraseña válida")
        void debeVerificarContraseñaValida() {
            String plainPassword = "admin123";
            String hash = CryptoUtil.md5(plainPassword);
            assertTrue(CryptoUtil.verificar(plainPassword, hash));
        }

        @Test
        @DisplayName("Debe rechazar contraseña incorrecta")
        void debeRechazarContraseñaIncorrecta() {
            String correctPassword = "admin123";
            String wrongPassword = "admin124";
            String hash = CryptoUtil.md5(correctPassword);
            
            assertFalse(CryptoUtil.verificar(wrongPassword, hash));
        }

        @Test
        @DisplayName("Debe rechazar contraseña nula vs hash válido")
        void debeRechazarContraseñaNula() {
            String hash = CryptoUtil.md5("test");
            assertFalse(CryptoUtil.verificar(null, hash));
        }

        @Test
        @DisplayName("Debe rechazar hash nulo vs contraseña válida")
        void debeRechazarHashNulo() {
            assertFalse(CryptoUtil.verificar("test", null));
        }

        @Test
        @DisplayName("Debe rechazar ambos nulos")
        void debeRechazarAmbosNulos() {
            assertFalse(CryptoUtil.verificar(null, null));
        }

        @Test
        @DisplayName("Debe ser sensible a mayúsculas/minúsculas")
        void debeSerSensibleAMayusculas() {
            String hash = CryptoUtil.md5("Password");
            assertFalse(CryptoUtil.verificar("password", hash));
            assertTrue(CryptoUtil.verificar("Password", hash));
        }

        @Test
        @DisplayName("Debe rechazar hash incompleto")
        void debeRechazarHashIncompleto() {
            String hash = "5d41402abc4b2a76b9719d911017c59"; // 31 chars en lugar de 32
            String password = "hello";
            assertFalse(CryptoUtil.verificar(password, hash));
        }

        @Test
        @DisplayName("Debe rechazar hash con caracteres inválidos")
        void debeRechazarHashInvalido() {
            String invalidHash = "5d41402abc4b2a76b9719d911017c5gg"; // 'gg' son inválidos en hex
            String password = "hello";
            assertFalse(CryptoUtil.verificar(password, invalidHash));
        }

        @Test
        @DisplayName("Debe funcionar con cadenas vacías")
        void debeVerificarCadenasVacias() {
            String hash = CryptoUtil.md5("");
            assertTrue(CryptoUtil.verificar("", hash));
            assertFalse(CryptoUtil.verificar("algo", hash));
        }

        @Test
        @DisplayName("Debe verificar hashes para usuarios del sistema")
        void debeVerificarHashesDelSistema() {
            // Estos son hashes reales del sistema
            assertTrue(CryptoUtil.verificar("admin23", "21232f297a57a5a743894a0e4a801fc3")); // ADMIN
            assertTrue(CryptoUtil.verificar("productos19", "123456")); // Hash dummy - cambiar con el real
            assertTrue(CryptoUtil.verificar("almacenes11", "654321")); // Hash dummy - cambiar con el real
        }

        @Test
        @DisplayName("Debe ser consistente en múltiples verificaciones")
        void debeSerConsistente() {
            String password = "test_password";
            String hash = CryptoUtil.md5(password);
            
            assertTrue(CryptoUtil.verificar(password, hash));
            assertTrue(CryptoUtil.verificar(password, hash));
            assertTrue(CryptoUtil.verificar(password, hash));
        }

        @Test
        @DisplayName("Debe verificar hashes con caracteres especiales")
        void debeVerificarConCaracteresEspeciales() {
            String password = "paßwörd!@#";
            String hash = CryptoUtil.md5(password);
            assertTrue(CryptoUtil.verificar(password, hash));
        }

        @Test
        @DisplayName("Debe rechazar contraseña con espacios adicionales")
        void debeRechazarEspaciosAdicionales() {
            String password = "mypassword";
            String hash = CryptoUtil.md5(password);
            
            assertFalse(CryptoUtil.verificar(" mypassword", hash));
            assertFalse(CryptoUtil.verificar("mypassword ", hash));
            assertFalse(CryptoUtil.verificar(" mypassword ", hash));
        }
    }

    @Nested
    @DisplayName("Casos de uso realistas de seguridad")
    class CasosSeguridad {
        
        @Test
        @DisplayName("Simular registro de usuario")
        void simularRegistroUsuario() {
            String usuarioInput = "sofia";
            String passwordInput = "password123";
            
            // Simular guardado en BD (hash)
            String hashGuardado = CryptoUtil.md5(passwordInput);
            
            // Simular login con contraseña correcta
            assertTrue(CryptoUtil.verificar(passwordInput, hashGuardado));
            
            // Simular login con contraseña incorrecta
            assertFalse(CryptoUtil.verificar("wrongpassword", hashGuardado));
        }

        @Test
        @DisplayName("Simular flujo de login del sistema")
        void simularFluxoLogin() {
            // Admin del sistema tiene este hash: md5("admin23")
            String hashAdmin = CryptoUtil.md5("admin23");
            
            // Usuario intenta login
            String passwordIntentado = "admin23";
            assertTrue(CryptoUtil.verificar(passwordIntentado, hashAdmin));
            
            // Intento fallido
            assertFalse(CryptoUtil.verificar("wrong_password", hashAdmin));
        }
    }
}