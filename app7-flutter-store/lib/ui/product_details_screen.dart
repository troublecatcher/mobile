import 'package:carousel_slider/carousel_slider.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:flutter_ecommerce/const/AppColors.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:fluttertoast/fluttertoast.dart';

class ProductDetails extends StatefulWidget {
  var _product;
  ProductDetails(this._product);
  @override
  _ProductDetailsState createState() => _ProductDetailsState();
}
List cart = [];
final FirebaseAuth _auth = FirebaseAuth.instance;
var currentUser = _auth.currentUser;
var _firestoreInstance = FirebaseFirestore.instance;
class _ProductDetailsState extends State<ProductDetails> {
  fetchCart() async {
    QuerySnapshot qn = await _firestoreInstance.collection("users-cart-items").doc(currentUser!.email).collection("items").get();
    setState(() {
      cart.length = 0;
      for (int i = 0; i < qn.docs.length; i++) {
        cart.add({
          "name": qn.docs[i]["name"],
          "quantity": qn.docs[i]["quantity"]
        });
      }
    });
    return qn.docs;
  }
  @override
  void initState() {
    fetchCart();
    super.initState();
  }
  Future addToCart() async {
    await fetchCart();
    CollectionReference _collectionRef =
        FirebaseFirestore.instance.collection("users-cart-items");

    bool isInCart = false;
    int quantity = 0;
    cart.forEach((element) {
      if(element["name"] == widget._product["product-name"]){
        isInCart = true;
        quantity = element["quantity"];
      }
    });
    String msg = "";
    if(isInCart == false) {
      quantity = 1;
      msg = "Добавлено в корзину!";
    }else{
      ++quantity;
      msg = "В корзине: ${quantity}";
    }
    _collectionRef
        .doc(currentUser!.email)
        .collection("items")
        .doc(widget._product["product-name"])
        .set({
      "quantity": quantity,
      "name": widget._product["product-name"],
      "price": widget._product["product-price"],
      "images": widget._product["product-img"],
    }).then((value) => Fluttertoast.showToast(msg: msg));
  }

  Future addToFavourite() async {
    final FirebaseAuth _auth = FirebaseAuth.instance;
    var currentUser = _auth.currentUser;
    CollectionReference _collectionRef =
    FirebaseFirestore.instance.collection("users-favourite-items");
    return _collectionRef
        .doc(currentUser!.email)
        .collection("items")
        .doc(widget._product["product-name"])
        .set({
      "name": widget._product["product-name"],
      "price": widget._product["product-price"],
      "images": widget._product["product-img"],
    }).then((value) => Fluttertoast.showToast(msg: "Добавлено в избранное!"));
  }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.transparent,
        elevation: 0,
        leading: Padding(
          padding: const EdgeInsets.all(8.0),
          child: CircleAvatar(
            backgroundColor: AppColors.deep_orange,
            child: IconButton(
                onPressed: () => Navigator.pop(context),
                icon: Icon(
                  Icons.arrow_back,
                  color: Colors.white,
                )),
          ),
        ),
        actions: [
          StreamBuilder(
            stream: FirebaseFirestore.instance.collection("users-favourite-items").doc(FirebaseAuth.instance.currentUser!.email)
                .collection("items").where("name",isEqualTo: widget._product['product-name']).snapshots(),
            builder: (BuildContext context, AsyncSnapshot snapshot){
              if(snapshot.data==null){
                return Text("");
              }
              return Padding(
                padding: const EdgeInsets.only(right: 8),
                child: CircleAvatar(
                  backgroundColor: Colors.red,
                  child: IconButton(
                    onPressed: () => snapshot.data.docs.length==0?addToFavourite():Fluttertoast.showToast(msg: "Уже добавлено"),
                    icon: snapshot.data.docs.length==0? Icon(
                      Icons.favorite_outline,
                      color: Colors.white,
                    ):Icon(
                      Icons.favorite,
                      color: Colors.white,
                    ),
                  ),
                ),
              );
            },

          ),
        ],
      ),
      body: SafeArea(
          child: Padding(
        padding: const EdgeInsets.only(left: 12, right: 12, top: 10),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            AspectRatio(
              aspectRatio: 1,
              child: CarouselSlider(
                  items: widget._product['product-img']
                      .map<Widget>((item) => Padding(
                            padding: const EdgeInsets.only(left: 3, right: 3),
                            child: Container(
                              decoration: BoxDecoration(
                                  image: DecorationImage(
                                      image: NetworkImage(item),
                                      )),
                            ),
                          ))
                      .toList(),
                  options: CarouselOptions(
                      autoPlay: false,
                      autoPlayInterval: Duration(seconds: 5),
                      enlargeCenterPage: true,
                      viewportFraction: 0.8,
                      enlargeStrategy: CenterPageEnlargeStrategy.height,
                      onPageChanged: (val, carouselPageChangedReason) {
                        setState(() {});
                      })),
            ),
            Text(
              widget._product['product-name'],
              style: TextStyle(fontWeight: FontWeight.bold, fontSize: 25),
            ),
            Text(widget._product['product-description']),
            SizedBox(
              height: 10,
            ),
            Text(
              "${widget._product['product-price'].toString()} руб.",
              style: TextStyle(
                  fontWeight: FontWeight.bold, fontSize: 30, color: Colors.red),
            ),
            Divider(),
            SizedBox(
              width: 1.sw,
              height: 56.h,
              child: ElevatedButton(
                onPressed: () => addToCart(),
                child: Text(
                  "В корзину",
                  style: TextStyle(color: Colors.white, fontSize: 18.sp),
                ),
                style: ElevatedButton.styleFrom(
                  backgroundColor: AppColors.deep_orange,
                  elevation: 3,
                ),
              ),
            ),
          ],
        ),
      )),
    );
  }
}
