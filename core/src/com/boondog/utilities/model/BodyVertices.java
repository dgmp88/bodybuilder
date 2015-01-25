package com.boondog.utilities.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.boondog.imports.math.MyVectors;

public class BodyVertices {
	public final static float vertSize = 4; // This value is only used during building the constellations
	Array<Vector2> vertices = new Array<Vector2>();
	
	float aspectRatio; // Ideally this. width/height
	
	public void addVertex(Vector2 tmp) {
		vertices.add(tmp);
	}

	public Array<Vector2> getVertices() {
		return vertices;
	}

	public void removeVertex(Vector2 star) {
		vertices.removeValue(star, true);
	}
	

	public BodyVertices clone() {
		BodyVertices clone = new BodyVertices();
		for (Vector2 star : vertices) {
			clone.addVertex(star.cpy());
		}
		return clone;
		
	}
	
	public void norm() {	
		MyVectors.norm(vertices);
	}


	public void print() {
		System.out.println("Vertices:");
		
		for (Vector2 star : vertices) {
			System.out.println(star);
		}
		
		float minX = MyVectors.getMin(vertices,true), maxX = MyVectors.getMax(vertices,true);
		float minY = MyVectors.getMin(vertices,false), maxY = MyVectors.getMax(vertices,false);
		System.out.println("minX: " + minX + " maxX: " + maxX + " minY: " + minY + " maxY: " + maxY + " meanX: " + (minX+maxX)/2 + " meanY: " + (minY+maxY)/2);
	}

	public Vector2 getVertex(int i) {
		return vertices.get(i);
	}


	public void rotate(Vector2 rotateAbout, float deg) {
		for (Vector2 v : vertices) {
			v = rotateAbout(v,rotateAbout,deg);
		}
	}
	
	private Vector2 rotateAbout(Vector2 toRotate, Vector2 rotateAbout, float degrees) {
		toRotate.sub(rotateAbout);
		toRotate.rotate(degrees);
		toRotate.add(rotateAbout);
		return toRotate;
	}
	
	public float getAspectRatio() {
		return aspectRatio;
	}
	
	public void setAspectRatio() {
		float width = MyVectors.getMax(vertices, true) - MyVectors.getMin(vertices, true);
		float height= MyVectors.getMax(vertices, false)- MyVectors.getMin(vertices, false);
		aspectRatio = width/height;
	}
	
	public void centerIn(Vector2 boxPos, Vector2 targetSize) {
		// Figure out how big it should be.
		float width = 1, height = width/aspectRatio; // Set the ideal size here.
		Vector2 size = Scaling.fit.apply(width, height, targetSize.x, targetSize.y);
		
		if (aspectRatio == 0) {
			System.out.println("Something went wrong with loading the aspect ratio");
		}
		
		
		// Move it up or down a bit so it's centered
		Vector2 pos = boxPos.cpy();
		if (size.x < targetSize.x) {
			pos.x += (targetSize.x - size.x)/2;
		} else if (size.y < targetSize.y) {
			pos.y += (targetSize.y - size.y)/2;
		}
		
		// Now scale!
		MyVectors.scaleTo(vertices, pos.x, pos.y,
				pos.x + size.x, pos.y + size.y);
	}
}
