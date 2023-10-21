package com.servlets;

import com.sc.controladores.DepartamentoController;
import java.io.IOException;
import com.sc.interfaces.IDepartamentoController;
import com.sc.datatypes.dataDepartamento;
import com.sc.excepciones.ParametrosInvalidosExcepcion;
import com.sc.excepciones.UsuarioYaExisteExcepcion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@WebServlet("/DepartamentoControllerServlet")
public class DepartamentoControllerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    IDepartamentoController controller = new DepartamentoController();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            response.getWriter().write("Action is null.");
            return;
        }

        switch (action) {
            case "crearDepartamento":
                String nombre = request.getParameter("nombre");
                String descripcion = request.getParameter("descripcion");
                String url = request.getParameter("url");

                try {
                    controller.crearDepartamento(nombre, descripcion, url);
                    response.getWriter().write("Departamento creado exitosamente.");
                } catch (ParametrosInvalidosExcepcion | UsuarioYaExisteExcepcion e) {
                    response.getWriter().write("Error al crear el departamento: " + e.getMessage());
                }
                break;

            case "listarDepartamentos":
                List<dataDepartamento> departamentos = controller.listarDepartamentos();
                JSONArray jsonArray = new JSONArray();

                for (dataDepartamento departamento : departamentos) {
                    JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("nombre", departamento.getNombre());
            } catch (JSONException ex) {
                Logger.getLogger(DepartamentoControllerServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                jsonObject.put("descripcion", departamento.getDescripcion());
            } catch (JSONException ex) {
                Logger.getLogger(DepartamentoControllerServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                jsonObject.put("url", departamento.getUrl());
            } catch (JSONException ex) {
                Logger.getLogger(DepartamentoControllerServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
                    jsonArray.put(jsonObject);
                }

                response.getWriter().write(jsonArray.toString());
                break;

            default:
                response.getWriter().write("Accion no valida.");
                break;
        }
    }
}