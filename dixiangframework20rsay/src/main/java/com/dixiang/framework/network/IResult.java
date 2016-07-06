package com.dixiang.framework.network;

public interface IResult<T> {

   String status();
   String errorCode();
   String errorMsg();
   T getResults();
   
}
