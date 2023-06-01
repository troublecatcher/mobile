import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_ecommerce/ui/bottom_nav_controller.dart';
import 'package:flutter_ecommerce/ui/bottom_nav_pages/home.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:fluttertoast/fluttertoast.dart';

class Purchase extends StatefulWidget {
  @override
  _PurchaseState createState() => _PurchaseState();
}
var cart = [];
class _PurchaseState extends State<Purchase> {
  getCart() async {
    QuerySnapshot qn = await FirebaseFirestore.
    instance.collection("users-cart-items").
    doc(FirebaseAuth.instance.currentUser!.email).collection("items").get();
    setState(() {
      for (int i = 0; i < qn.docs.length; i++) {
        cart.add({
          "name": qn.docs[i]["name"],
          "quantity": qn.docs[i]["quantity"],
          "price": qn.docs[i]["price"]
        });
      }
    });
    return qn.docs;
  }
  confirmOrder() async {
    await getCart();

    num sum = 0;
    cart.forEach((element) {
      sum += element["quantity"] * element["price"];
    });
    cart.forEach((element) {
      FirebaseFirestore.instance.collection("orders")
          .doc(FirebaseAuth.instance.currentUser!.email)
          .collection('orders')
          .doc(sum.toString()).set({
        '${element['name']}': {
          'price': element['price'],
          'quantity': element['quantity'],
          'subtotal': element['price'] * element['quantity'],
        },
      },SetOptions(merge: true));
      FirebaseFirestore.instance.collection("users-cart-items").
      doc(FirebaseAuth.instance.currentUser!.email).collection("items").doc(element["name"]).delete();
    });
    sum = 0;
  }
  @override
  void initState() {
    super.initState();
    confirmOrder();
  }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(child: Padding(
        padding: const EdgeInsets.all(20.0),
        child: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              Text(
              "Спасибо за покупку =)",
              textAlign: TextAlign.center,
              style: TextStyle(
                  color: Colors.deepOrange,
                  fontWeight: FontWeight.bold,
                  fontSize: 44.sp),
            ),
              GestureDetector(
                child: Text(
                  "Продолжаем шоппинг?",
                  textAlign: TextAlign.center,
                  style: TextStyle(
                      color: Colors.orange,
                      fontSize: 20.sp),
                ),
                onTap: () {
                  Navigator.push(
                      context,
                      CupertinoPageRoute(
                          builder: (context) => BottomNavController()));
                },
              )],
          ),
        )
      )),
    );
  }
}
