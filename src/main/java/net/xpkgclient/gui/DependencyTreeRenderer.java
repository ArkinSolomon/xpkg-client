/*
 * Copyright (c) 2023. Arkin Solomon.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied limitations under the License.
 */

package net.xpkgclient.gui;

import lombok.Getter;
import net.xpkgclient.packagemanager.DependencyTree;
import net.xpkgclient.packagemanager.PackageDependency;
import net.xpkgclient.packagemanager.PackageNode;
import org.jungrapht.visualization.VisualizationViewer;
import org.jungrapht.visualization.decorators.EdgeShape;
import org.jungrapht.visualization.layout.algorithms.EiglspergerLayoutAlgorithm;
import org.jungrapht.visualization.renderers.Renderer;

import javax.swing.*;
import java.awt.*;

/**
 * This class renders the dependency tree.
 */
public class DependencyTreeRenderer {

    /**
     * The displayed frame.
     *
     * @returns The reference to the currently displayed frame.
     */
    @Getter
    private final JFrame frame;

    /**
     * Create and display a dependency tree in a new window.
     *
     * @param tree The dependency tree to display.
     */
    public DependencyTreeRenderer(DependencyTree tree) {
        VisualizationViewer<PackageNode, PackageDependency> vv = VisualizationViewer.builder(tree.getGraph()).build();

        EiglspergerLayoutAlgorithm<PackageNode, PackageDependency> layoutAlgorithm = EiglspergerLayoutAlgorithm.<PackageNode, PackageDependency>edgeAwareBuilder().build();

        layoutAlgorithm.setVertexBoundsFunction(vv.getRenderContext().getVertexBoundsFunction());
        vv.getVisualizationModel().setLayoutAlgorithm(layoutAlgorithm);
        vv.getRenderContext().setVertexLabelPosition(Renderer.VertexLabel.Position.CNTR);
        vv.getRenderContext().setVertexLabelDrawPaintFunction(v -> Color.green);
        vv.getRenderContext().setVertexLabelFunction(PackageNode::getPackageId);

        vv.getRenderContext().setEdgeShapeFunction(new EdgeShape.QuadCurve<>());
        vv.getRenderContext()
                .setEdgeLabelFunction(e -> e.selection().toString());
        vv.setEdgeToolTipFunction(Record::toString);

        frame = new JFrame();
        frame.getContentPane().add(vv.getComponent());
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
