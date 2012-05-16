/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kusoft.im;

import com.kusoft.im.net.Connection;



/**
 *
 * @author zhanhx
 */
public class Main {
    public static void main(String [] args) {
        new Connection.Daemon(80).start();
    }
    
}
