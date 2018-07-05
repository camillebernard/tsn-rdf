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


		if (request.getParameter("carte") != null) {
			test = new MapController(request.getParameter("carte"));
			//List<String> territorialUnitList = MapLayers.query("NUTS1999");
			
		}else {
			System.out.println("ERROR ! No request param.");
			test = new MapController("NUTS1999");
		}
		//request.setAttribute("test", test);
		//this.getServletContext().getRequestDispatcher("/TsnRDF.jsp").forward(request, response);


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().print(test.getFeatureCollection());


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
