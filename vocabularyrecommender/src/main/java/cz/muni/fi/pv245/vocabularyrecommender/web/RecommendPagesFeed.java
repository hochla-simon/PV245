package cz.muni.fi.pv245.vocabularyrecommender.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/recommendPagesFeed"})
public class RecommendPagesFeed extends HttpServlet {
    
    private static final String code = "AQAXgoTKpUc-5YIysCTHt-HxnFFXoRQeU4NFhRI126RxN3bjIoIHLAXXn25VbRPikMmQA9gpWN-eGQ44kegvKQnrA0Jl7w9UlXKedi2afWf03C9qLnSN98vR25Xlpgmxd47B_yqjoHYq3mIdloMAoq_tgTzeO-QAoAjc68hoM5mLYkAfamN4m7wW4zARfW-PkHL4hSgsB1RUoBHu4IIh_guCKJ9CIWdPxaNoUDYface-NOfSwy_U5w8CsdlOs8H8eBZ8Xe89eSx4YCB6WDH0XGJ3fpm6LrZhnk4I195R0zHWn_7wA6lKt3SoiBHysKZUmdraAjdrdcCPV_2hPHICMNBI";

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setAttribute("code", code);
        req.getRequestDispatcher("/RecommendPagesFeed.jsp").forward(req, res);
    }
}
