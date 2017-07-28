package fr.imag.steamer.tsn.tsnrdf.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fr.imag.steamer.tsn.tsnrdf.beans.MapController;

/**
 * Servlet implementation class AjaxTest
 */
@WebServlet(name = "AjaxTest", urlPatterns = {"/ajaxtest"})
public class AjaxTest extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AjaxTest() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        MapController test = null;
        HttpSession session = request.getSession();
       
       // if (request.getParameter("spatiale") == null && request.getParameter("phoneme") == null) {
            test = new MapController(request.getParameter("carte"));
//        } else if (request.getParameter("spatiale") != null && request.getParameter("phoneme") == null) {
//            if (request.getParameter("select") == null || request.getParameter("select").equals("point")) {
//                if (request.getParameter("lat") == null || request.getParameter("lon") == null) {
//                    test = new MapController(request.getParameter("carte"), categoriesMap);
//                } else if (request.getParameter("rayon") == null) {
//                    test = new MapController(request.getParameter("lat"), request.getParameter("lon"), request.getParameter("carte"), categoriesMap);
//                } else if (request.getParameter("rayon") == null || request.getParameter("rayon").equals("") || Integer.parseInt(request.getParameter("rayon")) == 0) {
//                    test = new MapController(request.getParameter("lat"), request.getParameter("lon"), request.getParameter("carte"), categoriesMap);
//                } else {
//                    test = new MapController(request.getParameter("lat"), request.getParameter("lon"), request.getParameter("rayon"), request.getParameter("carte"), categoriesMap);
//                }
//            } else {
//                test = new MapController(request.getParameter("region"), request.getParameter("carte"), categoriesMap);
//            }
//
//        } else if (request.getParameter("spatiale") == null && request.getParameter("phoneme") != null) {
//            if (request.getParameter("api") == null || request.getParameter("api").equals("")) {
//                test = new MapController(request.getParameter("carte"), categoriesMap);
//            } else {
//                test = new MapController(request.getParameter("lat"), request.getParameter("lon"), request.getParameter("rayon"),
//                        request.getParameter("api"), request.getParameter("carte"), false, categoriesMap);
//            }
//
//        } else if (request.getParameter("spatiale") != null && request.getParameter("phoneme") != null) {
//            if (request.getParameter("select") == null || request.getParameter("select").equals("point")) {
//                if (request.getParameter("rayon") == null || request.getParameter("rayon").equals("") || Integer.parseInt(request.getParameter("rayon")) == 0) {
//                    test = new MapController(request.getParameter("lat"), request.getParameter("lon"), request.getParameter("rayon"), request.getParameter("api"),
//                            request.getParameter("carte"), false, categoriesMap);
//                } else {
//                    test = new MapController(request.getParameter("lat"), request.getParameter("lon"), request.getParameter("rayon"), request.getParameter("api"),
//                            request.getParameter("carte"), true, categoriesMap);
//                }
//            } else {
//                test = new MapController(request.getParameter("region"), request.getParameter("carte"), true, request.getParameter("api"),
//                        categoriesMap);
//            }
//        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
//        if (request.getParameter("spatiale") != null &&request.getParameter("select") != null && request.getParameter("select").equals("region")) {
//            response.getWriter().print(test.getPolygon());
//        } else {
            response.getWriter().print(test.getFeatureCollection());
//        }

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
