package com.guatex.igconfiguraciones.archivos;

import com.guatex.igconfiguraciones.entidades.E_Usuario;
import com.guatex.igconfiguraciones.loggs.ArchivoLogs;
import com.guatex.igconfiguraciones.principal.GTXConfiguracionIG;
import com.guatex.igconfiguraciones.util.Parametros;
import com.guatex.igconfiguraciones.util.UtilesArchivos;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 *
 * @author ESTEFANIECM
 */
public class A_Usuarios {

    private final String carpetaArchivo = Parametros.getRutaRaiz() + "SERVICIO_IMPRESION_GUATEX";
    private final String nombreArchivo = "datosUsuarios.txt";
    private final String rutaArchivo = carpetaArchivo + "\\M_USUARIOS\\" + nombreArchivo;
    private final String rutaBK = Parametros.getRutaRaiz() + "BACKUP_IMPRESION_GUATEX\\BK_USUARIOS\\" + nombreArchivo;

    public ArrayList<E_Usuario> usuariosRegistrados() {
        return usuariosRegistrados(false);
    }

    private ArrayList<E_Usuario> usuariosRegistrados(boolean desdeBackup) {
        ArrayList<E_Usuario> lista = new ArrayList<>();
        try {
            File archivoTxt = new File(rutaArchivo);

            if (!archivoTxt.exists()) {
                System.out.println("x/x/x El archivo de Usuarios no existe x/x/x");
                archivoTxt.getParentFile().mkdirs();
                archivoTxt.createNewFile();
                System.out.println("      El archivo de Usuarios fue creado. Intentando restaurar desde BK...");

                if (!desdeBackup && restaurarDesdeBK()) {
                    return usuariosRegistrados(true);
                } else {
                    System.out.println("** No se pudo restaurar desde BK o archivo BK también vacío **");
                }

            } else if (archivoTxt.length() == 0) {
                System.out.println("** Archivo de Usuarios vacío **");

                if (!desdeBackup && restaurarDesdeBK()) {
                    System.out.println("   Restauración desde BK exitosa. Releyendo...");
                    return usuariosRegistrados(true);
                } else {
                    System.out.println("** No se pudo restaurar o BK también vacío. Se retorna lista vacía **");
                }

            } else {
                System.out.println(">> Archivo de Usuarios con datos <<");
                try (BufferedReader reader = new BufferedReader(new FileReader(archivoTxt))) {
                    String registro;
                    while ((registro = reader.readLine()) != null) {
                        E_Usuario imp = new E_Usuario().getUsuario(registro);
                        lista.add(imp);
                    }
                }
            }

        } catch (IOException e) {
            ArchivoLogs.getInstance().grabaLogFileAdministrador(
                    "------ Excepción - Archivo usuarios - [" + e.getLocalizedMessage() + "]", true);
            e.printStackTrace();
        }

        return lista;
    }

    public boolean guardarUsuariosArchivo() {
        boolean respuesta = false;
        // Verificar si el archivo existe
        File archivoTxt = new File(rutaArchivo);
        boolean archivoExiste = archivoTxt.exists();
        if (archivoExiste) {
            System.out.println("--- archivo sí existe");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoTxt))) {
                for (E_Usuario u : GTXConfiguracionIG.ListaUsuarios) {
                    writer.write(u.usuarioTexto());
                    writer.newLine();
                }
                respuesta = true;
            } catch (IOException e) {
                ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Excepción - Guardar archivo usuarios - [" + e.getLocalizedMessage() + "]", true);
                respuesta = false;
                // Intentar restaurar desde el archivo de respaldo
                if (restaurarDesdeBK()) {
                    // Intentar guardar de nuevo
                    respuesta = guardarUsuariosArchivo();
                }
            }
        } else {
            respuesta = false;
            // Intentar restaurar desde el archivo de respaldo
            if (restaurarDesdeBK()) {
                // Intentar guardar de nuevo
                respuesta = guardarUsuariosArchivo();
            }
        }
        return respuesta;
    }

    public void realizarBKarchivo() {
        ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Backup Archivo usuarios", false);
        Path rutaOriginal = Paths.get(rutaArchivo);
        Path rutaCopia = Paths.get(rutaBK);
        UtilesArchivos arc = new UtilesArchivos();
        try {
            // Crear la carpeta de destino si no existe
            if (!Files.exists(rutaCopia.getParent())) {
                Files.createDirectories(rutaCopia.getParent());
            }
            arc.ocultarDirectorio(rutaCopia.getParent());
            // Ocultar la carpeta "BACKUP"
            arc.ocultarDirectorio(Paths.get(Parametros.getRutaRaiz() + "BACKUP_IMPRESION_GUATEX"));

            // Verificar que el archivo original exista y sea un archivo regular
            if (Files.exists(rutaOriginal) && Files.isRegularFile(rutaOriginal)) {
                // Copiar el archivo desde el origen hasta el destino, reemplazando si ya existe
                Files.copy(rutaOriginal, rutaCopia, StandardCopyOption.REPLACE_EXISTING);
                arc.ocultarArchivo(rutaCopia);
            }
        } catch (IOException e) {
            ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Excepción - BK usuarios - [" + e.getLocalizedMessage() + "]", true);
            e.printStackTrace();
        }
    }

    private boolean restaurarDesdeBK() {
        Path rutaOriginal = Paths.get(rutaArchivo);
        Path rutaCopia = Paths.get(rutaBK);

        try {
            if (Files.exists(rutaCopia) && Files.isRegularFile(rutaCopia)) {
                // Crear la carpeta de destino si no existe
                if (!Files.exists(rutaOriginal.getParent())) {
                    Files.createDirectories(rutaOriginal.getParent());
                }
                // Copiar el archivo de respaldo a la ruta original
                Files.copy(rutaCopia, rutaOriginal, StandardCopyOption.REPLACE_EXISTING);
                // Quitar el atributo de oculto del archivo restaurado
                Files.setAttribute(rutaOriginal, "dos:hidden", false);
                ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Se realizó restauración de usuarios desde BK", false);
                return true;
            }
        } catch (IOException e) {
            ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Excepción - Restaurar usuarios - [" + e.getLocalizedMessage() + "]", true);
            System.err.println("Error en A_Usuarios -> restaurarDesdeBK: " + e.getLocalizedMessage());
        }
        return false;
    }

}
