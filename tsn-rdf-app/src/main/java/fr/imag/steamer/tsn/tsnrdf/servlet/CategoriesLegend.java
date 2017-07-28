/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.imag.steamer.tsn.tsnrdf.servlet;

import fr.imag.steamer.tsn.tsnrdf.beans.ResponseCategory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Philippe GENOUD - Université Grenoble Alpes - Lab LIG-Steamer
 */
@WebServlet(name = "CategoriesLegend", urlPatterns = {"/categoriesLegend"})
public class CategoriesLegend extends HttpServlet {

    private final String[] categoriesColors = {"#ffffcc", "#ffeda0", "#fed976", "#feb24c", "#fd8d3c", "#fc4e2a", "#e31a1c", "#bd0026", "#800026"};

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        Map<String, ResponseCategory> categoriesMap = (Map<String, ResponseCategory>) session.getAttribute("categories");
        List<ResponseCategory> categoriesList = new ArrayList<>(categoriesMap.values());
        Collections.sort(categoriesList);

        PrintWriter out = response.getWriter();
        int legendsize = Math.min(categoriesColors.length, categoriesList.size());
        int nbRepTotal = 0;
        for (ResponseCategory cat : categoriesList) {
            nbRepTotal += cat.getSize();
        }
        int nbRep = 0;
        for (int rank = 0; rank < legendsize - 1; rank++) {
            out.println("<dt><input type=\"checkbox\" class=\"categorie\" id=\"cat" + rank + "\" value=\"ON\" checked=\"checked\" />&nbsp;<span class=\"legendColor\" style=\"background-color:"
                    + categoriesColors[legendsize - 1 - rank] + ";\">&nbsp;&nbsp;&nbsp;&nbsp;<span/></dt>\n"
                    + "<dd id=\"dd" + rank + "\">" + categoriesList.get(rank).getPhoneme() + " ("
                    + categoriesList.get(rank).getSize() + ")"
                    + "</dd>\n");
            nbRep += categoriesList.get(rank).getSize();
        }
        if (legendsize < categoriesList.size()) {
            out.println("<dt><input type=\"checkbox\" class=\"categorie\" id=\"cat" + (legendsize - 1) + "\" value=\"ON\" checked=\"checked\" />&nbsp;<span style=\"background-color:" + categoriesColors[0]
                    + ";\" class=\"legendColor\">&nbsp;&nbsp;&nbsp;&nbsp;<span/></dt>"
                    + "<dd id=\"dd" + (legendsize - 1) + "\">autre (" + (nbRepTotal - nbRep) +")<br>"
                    + (1 + categoriesList.size() - categoriesColors.length) + " phonèmes diff.<br>"
                    + "(nb occurences <= "
                    + categoriesList.get(legendsize - 1).getSize() + ")"
                    + "</dd>\n");
        } else {
            out.println("<dt><input type=\"checkbox\" class=\"categorie\" id=\"cat" + (legendsize - 1) + "\" value=\"ON\" checked=\"checked\" />&nbsp;<span class=\"legendColor\" style=\"background-color:"
                    + categoriesColors[0] + ";\">&nbsp;&nbsp;&nbsp;&nbsp;<span/></dt>\n"
                    + "<dd id=\"dd" + (legendsize - 1) + "\">" + categoriesList.get((legendsize - 1)).getPhoneme() + " ("
                    + categoriesList.get((legendsize - 1)).getSize() + ")"
                    + "</dd>\n");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

