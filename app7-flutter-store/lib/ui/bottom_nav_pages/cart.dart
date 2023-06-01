import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_ecommerce/const/AppColors.dart';
import 'package:flutter_ecommerce/ui/purchase_screen.dart';
import 'package:flutter_ecommerce/ui/splash_screen.dart';
import 'package:flutter_ecommerce/widgets/fetchCart.dart';

class Cart extends StatefulWidget {
  @override
  _CartState createState() => _CartState();
}

class _CartState extends State<Cart> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: fetchCart("users-cart-items"),
      ),
      floatingActionButton: ElevatedButton(
        onPressed: () =>{
          Navigator.push(context, CupertinoPageRoute(builder: (_)=>SplashScreen()))},
        child: Text(
          "Купить",
          style: TextStyle(color: Colors.white, fontSize: 18),
        ),

        style: ElevatedButton.styleFrom(
          backgroundColor: AppColors.deep_orange,
          elevation: 10,
        ),
    ),
    );
  }
}
