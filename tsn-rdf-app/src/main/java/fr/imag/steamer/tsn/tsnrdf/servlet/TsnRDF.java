package fr.imag.steamer.tsn.tsnrdf.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fr.imag.steamer.tsn.tsnrdf.beans.MapController;
import fr.imag.steamer.tsn.tsnrdf.beans.MapLayers;

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
		// } else if (request.getParameter("spatiale") != null && request.getParameter("phoneme") == null) {
		// if (request.getParameter("select") == null || request.getParameter("select").equals("point")) {
		// if (request.getParameter("lat") == null || request.getParameter("lon") == null) {
		// test = new MapController(request.getParameter("carte"), categoriesMap);
		// } else if (request.getParameter("rayon") == null) {
		// test = new MapController(request.getParameter("lat"), request.getParameter("lon"), request.getParameter("carte"), categoriesMap);
		// } else if (request.getParameter("rayon").equals("") || request.getParameter("rayon") == null || Integer.parseInt(request.getParameter("rayon")) == 0) {
		// test = new MapController(request.getParameter("lat"), request.getParameter("lon"), request.getParameter("carte"), categoriesMap);
		// } else {
		// test = new MapController(request.getParameter("lat"), request.getParameter("lon"), request.getParameter("rayon"), request.getParameter("carte"), categoriesMap);
		// }
		// } else {
		// //TODO REGION
		// }

		// } else if (request.getParameter("spatiale") == null && request.getParameter("phoneme") != null) {
		// if (request.getParameter("api") == null || request.getParameter("api").equals("")) {
		// test = new MapController(request.getParameter("carte"), categoriesMap);
		// } else {
		// test = new MapController(request.getParameter("lat"), request.getParameter("lon"), request.getParameter("rayon"),
		// request.getParameter("api"), request.getParameter("carte"), false, categoriesMap);
		// }
		//
		// } else if (request.getParameter("spatiale") != null && request.getParameter("phoneme") != null) {
		// if (request.getParameter("select") == null || request.getParameter("select").equals("point")) {
		// if (request.getParameter("rayon").equals("") || Integer.parseInt(request.getParameter("rayon")) == 0 || request.getParameter("rayon") == null) {
		// test = new MapController(request.getParameter("lat"), request.getParameter("lon"), request.getParameter("rayon"), request.getParameter("api"),
		// request.getParameter("carte"), false, categoriesMap);
		// } else {
		// test = new MapController(request.getParameter("lat"), request.getParameter("lon"), request.getParameter("rayon"), request.getParameter("api"),
		// request.getParameter("carte"), true, categoriesMap);
		// }
		// } else {
		// //TODO regionXphonem
		// }
		// }
		List<String> territorialUnitList = MapLayers.query("NUTS1999");
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
