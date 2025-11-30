/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.quanlyspa;

import View.MainView;
import Controller.AuthController; // Thêm import
import javax.swing.SwingUtilities;

/**
 *
 * @author MINH MAN
 */
public class QuanLySpa {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Tạo AuthController và truyền vào MainView
            AuthController authController = new AuthController();
            new MainView(authController).setVisible(true);
        });
    }
}