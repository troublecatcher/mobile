import 'package:flutter/material.dart';
import 'package:flutter_ecommerce/const/AppColors.dart';
import 'package:flutter_ecommerce/ui/bottom_nav_pages/cart.dart';
import 'package:flutter_ecommerce/ui/bottom_nav_pages/favourite.dart';
import 'package:flutter_ecommerce/ui/bottom_nav_pages/home.dart';
import 'package:flutter_ecommerce/ui/bottom_nav_pages/profile.dart';

class BottomNavController extends StatefulWidget {
  @override
  _BottomNavControllerState createState() => _BottomNavControllerState();
}

class _BottomNavControllerState extends State<BottomNavController> {
  final _pages = [
    Home(),
    Favourite(),
    Cart(),
    Profile(),
  ];
  var _currentIndex = 0;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.transparent,
        elevation: 0,
        title: Text(
          "Магазин электроники",
          style: TextStyle(color: Colors.black),
        ),
        centerTitle: true,
        automaticallyImplyLeading: false,
      ),
      bottomNavigationBar: BottomNavigationBar(
        elevation: 5,
        selectedItemColor: AppColors.deep_orange,
        backgroundColor: Colors.white,
        unselectedItemColor: Colors.grey,
        currentIndex: _currentIndex,
        selectedLabelStyle:
            TextStyle(color: Colors.black, fontWeight: FontWeight.bold),
        items: [
          BottomNavigationBarItem(
            icon: Icon(Icons.home),
            label: "Главная",
          ),
          BottomNavigationBarItem(
              icon: Icon(Icons.favorite_outline), label: "Избранное"),
          BottomNavigationBarItem(
            icon: Icon(Icons.shopping_cart),
            label: "Корзина",
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.person),
            label: "Профиль",
          ),
        ],
        onTap: (index) {
          setState(() {
            _currentIndex = index;
          });
        },
      ),
      body: _pages[_currentIndex],
    );
  }
}