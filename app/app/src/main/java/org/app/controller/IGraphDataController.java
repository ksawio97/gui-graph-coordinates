
package org.app.controller;

import org.app.model.Point;
import org.app.model.Vertex;

public interface IGraphDataController {
    public void registerOnVerticesChanged(IOnChangedListener<Vertex[]> onChanged);
    public void registerOnPointsChanged(IOnChangedListener<Point[]> onChanged);

    public Vertex[] gVerticesModels();

    public Point[] gPoints();
}
