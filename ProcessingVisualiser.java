package processingvisualiser;

import processing.core.*;
import ddf.minim.analysis.*;
import ddf.minim.*;

public class ProcessingVisualiser extends PApplet {

	Minim minim;
	AudioPlayer jingle;
	FFT fft;
	AudioInput in;
	float[] angle;
	float[] y, x;
	PFont Helveticastd;
		 
	public void setup() {
		  minim = new Minim(this);
		  in = minim.getLineIn(Minim.STEREO, 2048, 192000);
		  fft = new FFT(in.bufferSize(), in.sampleRate());
		  y = new float[fft.specSize()];
		  x = new float[fft.specSize()];
		  angle = new float[fft.specSize()];
		  noCursor();
		  frameRate(240);
		  Helveticastd = loadFont("HelveticaLTStd-Blk-99.vlw");		//In source directory for export
	}

	public void draw() {
		float loudestFrequency = 30;
		background(0);
		  
		  //Mouse clicks control background
		  if (mousePressed == true) {
			    if (mouseButton == LEFT) {
			        background(126); // Grey
			    } else if (mouseButton == RIGHT) { 
			    	background(255); // White
			    }
			  } else {
				   background(0); // Black (standard)
			  }
		  
		  fft.forward(in.mix);
		  
		  doubleAtomicSprocket();
		  
		  stroke(255, 0, 0, 128);
		  // draw the spectrum as a series of vertical lines
		  // I multiple the value of getBand by 4 
		  // so that we can see the lines better
		  //int specMargin = (screenWidth - fft.specSize()) / 2;	//Variable for centring the spectrum
		  for(int i = 0; i < fft.specSize(); i++)
		  {
			stroke(fft.specSize()-i, i, 0, (fft.specSize()-i)/4);
		    line(4*i+4, height, 4*i+4, (height - (fft.getBand(i)*4) - i/64));
		    if(fft.getBand(i)>loudestFrequency && fft.getBand(i)<fft.specSize()/3)loudestFrequency=fft.getBand(i);
		    //line(width-4*i, height, width-4*i, (height - (fft.getBand(i)*8)));
		  }
		  
		  
		 
		  /*
		  stroke(255);
		  // I draw the waveform by connecting 
		  // neighbor values with a line. I multiply 
		  // each of the values by 50 
		  // because the values in the buffers are normalized
		  // this means that they have values between -1 and 1. 
		  // If we don't scale them up our waveform 
		  // will look more or less like a straight line.
		  for(int i = 0; i < in.left.size() - 1; i++)
		  {
		    line(i, 50 + in.left.get(i)*50, i+1, 50 + in.left.get(i+1)*50);
		    line(i, 150 + in.right.get(i)*50, i+1, 150 + in.right.get(i+1)*50);
		  }
		  */
		  
		  
		  //Text that alters size with loudest frequency		  
		  int loudestFrequencyInt = (int)loudestFrequency;
		  textAlign(CENTER);
		  textFont(Helveticastd);
		  textSize(loudestFrequencyInt);
		  fill(126, 75);
		  text("YOSH", 960, 500);
		  text("SUCKS", 960, 580);
	}
	
	 public void settings() {
		 size(1920, 1080, P3D);				//HARD RESOLUTION FOR EXPORT	
		 fullScreen();
	 }
	 
	 
	void doubleAtomicSprocket() {
	  noStroke();
	  pushMatrix();
	  translate(width/2, height/2);
	  for (int i = 0; i < fft.specSize() ; i++) {
	    y[i] = y[i] + fft.getBand(i)/100;
	    x[i] = x[i] + fft.getFreq(i)/100;
	    angle[i] = angle[i] + fft.getFreq(i)/2000;
	    rotateX(sin(angle[i]/2));
	    rotateY(cos(angle[i]/2));
	    //    stroke(fft.getFreq(i)*2,0,fft.getBand(i)*2);
	    fill(fft.getFreq(i)*2, 0, fft.getBand(i)*2);
	    pushMatrix();
	    translate((x[i]+50)%width/3, (y[i]+50)%height/3);
	    box(fft.getBand(i)/20+fft.getFreq(i)/15);
	    popMatrix();
	  }
	  popMatrix();
	  pushMatrix();
	  translate(width/2, height/2, 0);
	  for (int i = 0; i < fft.specSize() ; i++) {
	    y[i] = y[i] + fft.getBand(i)/1000;
	    x[i] = x[i] + fft.getFreq(i)/1000;
	    angle[i] = angle[i] + fft.getFreq(i)/100000;
	    rotateX(sin(angle[i]/2));
	    rotateY(cos(angle[i]/2));
	    //    stroke(fft.getFreq(i)*2,0,fft.getBand(i)*2);
	    fill(0, 255-fft.getFreq(i)*2, 255-fft.getBand(i)*2);
	    pushMatrix();
	    translate((x[i]+250)%width, (y[i]+250)%height);
	    box(fft.getBand(i)/20+fft.getFreq(i)/15);
	    popMatrix();
	  }
	  popMatrix();
	}
	 
	public void stop()
	{
	  // always close Minim audio classes when you finish with them
	  jingle.close();
	  minim.stop();
	 
	  super.stop();
	}
	
	public static void main(String _args[]) {
		PApplet.main(new String[] { processingvisualiser.ProcessingVisualiser.class.getName() });
	}
}
