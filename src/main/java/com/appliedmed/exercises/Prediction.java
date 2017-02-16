/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appliedmed.exercises;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author zmichaels
 */
public class Prediction {

    private static class Point {

        final double x;
        final double y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Interpolates between the 2D points stored in the list
     *
     * @param points list of points
     * @return interpolated list of points
     */
    private static List<Point> interpolate(List<Point> points) {
        /*
         * TODO: implement this.
         * 40% of the List's points were deleted (set to null). Interpolate 
         * those points. You may either do this in-place or calculate a new List.
         */
		 
		 List<Point> res= new ArrayList <Point> ();
    	int count=0;
    	double x,y;
    	if(points.get(0)==null)
    		points.set(0, new Point(120,160));
    	for(int i=1;i<points.size();i++)
    	{
    		if(points.get(i)==null)
    			count++;
    		else
    		{	if(count==0)
    				res.add(points.get(i));
    		 if(count==1)
    			{	x= (points.get(i-2).x+points.get(i).x)/2;
				y= (points.get(i-2).y+points.get(i).y)/2;
				res.add(new Point(x,y));
				res.add(points.get(i));
				count=0;
			}else 
				if(count==2)
    			{	x= (points.get(i-3).x+points.get(i).x)/2;
    				y= (points.get(i-3).y+points.get(i).y)/2;
    				res.add(new Point((points.get(i-3).x+x)/2,(points.get(i-3).y+y)/2));
    				res.add(new Point((x+points.get(i).x)/2,(y+points.get(i).y)/2));
    				res.add(points.get(i));
    				count=0;
    			}
				else if(count==3)
	    			{	x= (points.get(i-4).x+points.get(i).x)/2;
	    				y= (points.get(i-4).y+points.get(i).y)/2;
	    				res.add(new Point((points.get(i-4).x+x)/2,(points.get(i-4).y+y)/2));
	    				res.add(new Point(x,y));
	    				res.add(new Point((x+points.get(i).x)/2,(y+points.get(i).y)/2));
	    				res.add(points.get(i));
	    				count=0;
	    			}
					else if(count==4)
	    			{
						x= (points.get(i-5).x+points.get(i).x)/2;
    				y= (points.get(i-5).y+points.get(i).y)/2;
    				Point t1=new Point((points.get(i-5).x+x)/2,(points.get(i-5).y+y)/2);
    				res.add(new Point((points.get(i-5).x+t1.x)/2,(points.get(i-5).y+t1.y)/2));
    				res.add(new Point((t1.x+x)/2,(t1.y+y)/2));
    				res.add(new Point((t1.x+x)/2,(t1.y+y)/2));
    				res.add(new Point((points.get(i).x+t1.x)/2,(points.get(i).y+t1.y)/2));
    				res.add(points.get(i));
    				count=0;
    			}
					else
    			{
    				res.add(points.get(i));
    				count=0;
    			}
    			
    		}
    			
    	}
        return res;
    }

    public static void main(String[] args) throws Exception {
        final Path pExpected = Paths.get("expected.csv");
        final List<Point> expected;

        // load the expected values
        try (BufferedReader in = Files.newBufferedReader(pExpected)) {
            expected = in.lines()
                    .skip(1)
                    .map(str -> str.split(","))
                    .map(p -> new Point(Double.parseDouble(p[0]), Double.parseDouble(p[1])))
                    .collect(Collectors.toList());
        }

        // delete 40% of the values
        final List<Point> actual = new ArrayList<>(expected);

        for (int i = 0; i < actual.size(); i++) {
            if (Math.random() < 0.4) {
                actual.set(i, null);
            }
        }

        // create the interpolated list 
        final List<Point> interpolated = interpolate(actual);

        final JFrame window = new JFrame("Interpolation");

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(640, 480);
        window.setVisible(true);
        window.setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                final int width = this.getWidth();
                final int height = this.getHeight();

                g.clearRect(0, 0, width, height);
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, width, height);

                final BufferedImage surface = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);

                // draw the expected values as cyan
                for (Point p : expected) {
                    final int x = (int) p.x;
                    final int y = (int) p.y;

                    surface.setRGB(x, y, 0xFF00FFFF);
                }

                // draw the interpolated values as yellow
                for (Point p : interpolated) {
                    if (p != null) {
                        final int x = (int) p.x;
                        final int y = (int) p.y;

                        surface.setRGB(x, y, 0xFFFFFF00);
                    }
                }

                g.drawImage(surface, 0, 0, width, height, 0, 0, 256, 256, null);
            }
        });
    }
}
