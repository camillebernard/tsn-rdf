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
 * Servlet implementation class TsnRDF
 */
@WebServlet(name = "TsnRDF", urlPatterns = { "/TsnRDF" })
public class TsnRDF extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TsnRDF() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		MapController test = null;
		HttpSession session = request.getSession();

		// if (request.getParameter("spatiale") == null && request.getParameter("phoneme") == null) {
		test = new MapController("NUTS1999");
//		response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        try (PrintWriter out = response.getWriter()) {
//             JSONObject geom = new JSONObject();
//             geom.put("geom", GeometryDAO.getGeometry(request.getParameter("region")));
//             out.println(geom);
//        }
//		
		
		//List<String> territorialUnitList = MapLayers.query("NUTS1999");
		request.setAttribute("test", test);
		this.getServletContext().getRequestDispatcher("/TsnRDF.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
