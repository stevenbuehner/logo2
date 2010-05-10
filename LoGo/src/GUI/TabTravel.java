/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import java.awt.FocusTraversalPolicy;
import javax.swing.JComponent;

/**
 *
 * @author tommy
 */
public class TabTravel {

    /**
     *
     * @param order Komponenten, geordnet nach gewuenschter Tab-Reihenfolge
     * @return
     */
    public static FocusTraversalPolicy getFocusTraversal(
            final JComponent order[]) {
        FocusTraversalPolicy policy = new FocusTraversalPolicy() {
            java.util.List list = java.util.Arrays.asList(order);


            public java.awt.Component getFirstComponent(
                    java.awt.Container focusCycleRoot) {
                return order[0];
            }

            public java.awt.Component getLastComponent(
                    java.awt.Container focusCycleRoot) {
                return order[order.length - 1];
            }

            public java.awt.Component getComponentAfter(
                    java.awt.Container focusCycleRoot,
                    java.awt.Component aComponent) {
                int index = list.indexOf(aComponent);
                return order[(index + 1) % order.length];
            }

            public java.awt.Component getComponentBefore(
                    java.awt.Container focusCycleRoot,
                    java.awt.Component aComponent) {
                int index = list.indexOf(aComponent);
                return order[(index - 1 + order.length) % order.length];
            }

            public java.awt.Component getDefaultComponent(
                    java.awt.Container focusCycleRoot) {
                return order[0];
            }

            @Override
            public java.awt.Component getInitialComponent(java.awt.Window window) {
                return order[0];
            }
        };
        return policy;
    }
}