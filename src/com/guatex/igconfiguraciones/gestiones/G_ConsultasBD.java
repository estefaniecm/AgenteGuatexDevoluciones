package com.guatex.igconfiguraciones.gestiones;

import com.guatex.igconfiguraciones.entidades.E_ImpresionesUsuario;
import com.guatex.igconfiguraciones.entidades.E_Usuario;
import com.google.gson.Gson;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import com.google.gson.reflect.TypeToken;
import com.guatex.igconfiguraciones.entidades.E_ActualizarImpresion;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;

/**
 *
 * @author ESTEFANIECM
 */
public class G_ConsultasBD {

    private final String urlAPI = "https://sig.guatex.gt";
//    private final String urlAPI = "http://localhost:8088";
//    private final String urlAPI = "https://desarrollo.guatex.gt";

    private final String prefijo = "/apidevimpresiones";

    public ArrayList<E_ImpresionesUsuario> consultarGuiasxImprimir(ArrayList<E_Usuario> usuarios) {
        ArrayList<E_ImpresionesUsuario> listaGuiasxImprimir = new ArrayList<>();
        try (CloseableHttpClient httpclient = HttpClients.createDefault();) {
            Gson gson = new Gson();
            String jsonUsuarios = gson.toJson(usuarios);
            System.out.println("usuario.... " + jsonUsuarios);
            HttpPost httppost = new HttpPost(urlAPI + prefijo + "/agente/impresiones");
            httppost.addHeader("Content-Type", "application/json");
            StringEntity entity = new StringEntity(jsonUsuarios, StandardCharsets.UTF_8);
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httppost.setEntity(entity);
            try (CloseableHttpResponse response = httpclient.execute(httppost)) {
                String jsonRespuesta = EntityUtils.toString(response.getEntity());
                System.out.println("Respuesta: " + jsonRespuesta);
                listaGuiasxImprimir = gson.fromJson(jsonRespuesta, new TypeToken<ArrayList<E_ImpresionesUsuario>>() {
                }.getType());

            }
        } catch (Exception ex) {
            listaGuiasxImprimir = null;
            System.err.println("Error consultarGuiasxImprimir() - " + ex.getLocalizedMessage());
        }
        return listaGuiasxImprimir;
    }

    public String actualizarEstadoImpresion(String noguia, String ip) {

        String respuesta = "";

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

            Gson gson = new Gson();

            E_ActualizarImpresion req = new E_ActualizarImpresion();
            req.setNoguia(noguia);
            req.setIp(ip);

            String json = gson.toJson(req);

            HttpPatch httppatch = new HttpPatch(
                    urlAPI + prefijo + "/agente/estado"
            );

            httppatch.addHeader("Content-Type", "application/json");

            StringEntity entity = new StringEntity(
                    json,
                    StandardCharsets.UTF_8
            );

            entity.setContentType(
                    new BasicHeader(HTTP.CONTENT_TYPE, "application/json")
            );

            httppatch.setEntity(entity);

            try (CloseableHttpResponse response = httpclient.execute(httppatch)) {

                String jsonRespuesta = EntityUtils.toString(
                        response.getEntity()
                );

                respuesta = gson.fromJson(
                        jsonRespuesta,
                        new TypeToken<String>() {
                        }.getType()
                );
            }

        } catch (Exception ex) {

            respuesta = "NO";

            System.err.println(
                    "Error actualizarEstadoImpresion() - "
                    + ex.getLocalizedMessage()
            );
        }

        return respuesta;
    }

    public String pruebaConexion() {
        String respuesta = "";
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(urlAPI + prefijo + "/conexion");
            try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
                respuesta = EntityUtils.toString(response.getEntity());
            }
        } catch (Exception ex) {
            respuesta = "NO";
            System.err.println("Error pruebaConexion() - " + ex.getLocalizedMessage());
        }
        return respuesta;
    }
}
