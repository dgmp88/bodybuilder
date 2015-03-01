package com.boondog.utilities.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ShortArray;

public class Triangulator {
	EarClippingTriangulator triangulator = new EarClippingTriangulator();
	FloatArray fPoints;
	ShortArray triangles;
	
	public Triangulator() {
		
	}
	
	public void makeTriangulation(Array<Vector2> points) {
		fPoints = new FloatArray();

		for (Vector2 v : points) {
			fPoints.add(v.x);
			fPoints.add(v.y);
		}
		
		triangles = triangulator.computeTriangles(fPoints);
		
	}

	public void draw(ShapeRenderer renderer, Array<Vector2> points) {
		if (triangles != null) {
			renderer.set(ShapeType.Line);
			renderer.setColor(Color.RED);
			for (int i = 0; i < triangles.size; i += 3) {
				int p1 = triangles.get(i);
				int p2 = triangles.get(i + 1);
				int p3 = triangles.get(i + 2);
				renderer.triangle( //
					points.get(p1).x, points.get(p1).y, //
					points.get(p2).x, points.get(p2).y, //
					points.get(p3).x, points.get(p3).y);
			}			
		}
	}

	public ShortArray getTriangulation() {
		return triangles;
	}
}
