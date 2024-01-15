package com.example.courseworkapp

data class Review(var rid : String ?= null, var rname : String ?= null,
                  var uid : String ?= null, var reviewtext : String ?= null,
                  var img : String ?= null, var location : String ?= null,
                  var score : Int ?= 0, var points : Int ?= 0,
                  var email : String ?= null)
