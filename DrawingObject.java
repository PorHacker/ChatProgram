package me;

import java.io.Serializable;

//그림 정보 저장 DrawingObject 클래스
public class DrawingObject implements Serializable{
   private static final long serialVersionUID = 1L;
   
   //사각형, 원 등의 도형 정보를 저장 변수들
   int upperLeftX, upperLeftY;
   int width, height;
   //선의 시작점과 끝점의 좌표 저장 변수들
   int x1, y1, x2, y2;
   boolean fill = false;
   String color;
   String shape;
   
   //사각형 또는 원을 생성할 때 사용되는 생성자
   DrawingObject(int upperLeftX, int upperLeftY, int width, int height,String shape,boolean fill,String c){
      this.upperLeftX = upperLeftX;
      this.upperLeftY = upperLeftY;
      this.width = width;
      this.shape = shape;
      this.height = height;
      this.fill = fill;
      this.color=c;
   }
   
   //선을 생성할 때 사용되는 생성자
   DrawingObject(int x1, int y1, int x2, int y2,String shape, String c){
      this.x1 = x1;
      this.x2 = x2;
      this.y1 = y1;
      this.y2 = y2;
      this.shape = shape;
      this.color=c;
   }

   public DrawingObject() {
   }
}