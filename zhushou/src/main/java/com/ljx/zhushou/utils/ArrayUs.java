package com.ljx.zhushou.utils;


    public class ArrayUs{
        public static void main(String[] args){
            int[] arr={13,45,7,3,9,468,4589,76,4}; //声明数组并赋值
            //调用遍历的方法
            print(arr); //在同一个类中,类名可以省略

            //调用获取最大值的方法
            System.out.println("最大元素为:"+max(arr));

            //调用获取最大值索引的方法
            System.out.println("最大元素的索引为:"+maxIndex(arr));

            //调用获取最小值的方法
            System.out.println("最小元素为:"+min(arr));

            //调用获取最小值索引的方法
            System.out.println("最小元素的索引为:"+minIndex(arr));

            //调用查找元素是否存在的方法
            System.out.println("查找元素的状态为:"+search(arr,9));

            //调用查找元素是否存在并返回索引方法
            System.out.println("查找元素的索引为:"+searchIndex(arr,9));

        }



        //遍历数组
        public static void print(int[] arr){
    /*for(int i:arr){ //使用加强for循环遍历
      System.out.print(arr[i]+"\t");
    }
    System.out.println; */
            for(int i=0;i<arr.length;i++){
                System.out.print(arr[i]+"\t");
            }
            System.out.println();
        }

        //获取最大值
        public static int max(int[] arr){
            int max=arr[0];
            for(int i=0;i<arr.length;i++){
                if(arr[i]>max){
                    max=arr[i];
                }
            }
            return max;
        }

        //获取最大值索引
        public static int maxIndex(int[] arr){
            int maxIndex=0;;
            for(int i=0;i<arr.length;i++){
                if(arr[i]>arr[maxIndex]){
                    maxIndex=i;
                }
            }
            return maxIndex;
        }

        //获取最小值
        public static int min(int[] arr){
            int min=arr[0];
            for(int i=0;i<arr.length;i++){
                if(arr[i]<min){
                    min=arr[i];
                }
            }
            return min;
        }

        //获取最小值索引
        public static int minIndex(int[] arr){
            int minIndex=0;;
            for(int i=0;i<arr.length;i++){
                if(arr[i]<arr[minIndex]){
                    minIndex=i;
                }
            }
            return minIndex;
        }

        //在数组中查找指定元素是否存在 ,如是存在返回true,不存在返回false
        public static boolean search(int[] arr,int number){
            for(int i=0;i<arr.length;i++){
                if(number==arr[i]){
                    return true;
                }
            }
            return false;
        }

        //在数组中查找指定元素是否存在 ,如是存在返回索引,不存在返回-1
        public static int searchIndex(int[] arr,int number){
            for(int i=0;i<arr.length;i++){
                if(number==arr[i]){
                    return i; //返回索引
                }
            }
            return -1;
        }
    }

