import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:flutter_ecommerce/widgets/fetchProducts.dart';
import 'package:fluttertoast/fluttertoast.dart';

import '../../widgets/fetchOrders.dart';

class Profile extends StatefulWidget {

  @override
  _ProfileState createState() => _ProfileState();
}

class _ProfileState extends State<Profile> {
  TextEditingController ?_nameController;
  TextEditingController ?_phoneController;
  List<ExpansionTile> expansionTiles = [];

  setDataToTextField(data){
    return Padding(
      padding: EdgeInsets.all(16.0),
      child: Column(
        children: [
          Text("Имя"),
          TextFormField(
            controller: _nameController = TextEditingController(text: data['name']),
          ),
          SizedBox(
            height: 15,
          ),
          Text("Телефон"),
          TextFormField(
            controller: _phoneController = TextEditingController(text: data['phone']),
          ),
          SizedBox(
            height: 15,
          ),
          ElevatedButton(
              onPressed: ()=>updateData(), child: Text("Обновить")),
          SizedBox(
            height: 15,
          ),
        ],
      ),
    );
  }

  updateData(){
    CollectionReference _collectionRef = FirebaseFirestore.instance.collection("users-form-data");
    return _collectionRef.doc(FirebaseAuth.instance.currentUser!.email).update(
        {
          "name":_nameController!.text,
          "phone":_phoneController!.text,
        }
    ).then((value) => Fluttertoast.showToast(msg: "Успешно обновлено!"));
  }

  populateExpansionPanel(var order){
    var container = [];
    num sum = 0;
    for(var orderItem in order.entries){
      // print(orderItem['price']);
      sum += orderItem.value['subtotal'];
      container.add(
          ListTile(
            title: Text(orderItem.key, style: TextStyle(color: Colors.black)),
            subtitle: Text(
                "${orderItem.value['price']} x ${orderItem.value['quantity']} = ${orderItem.value['subtotal']} руб.",
                style: TextStyle(color: Colors.black)),
          )
      );
    }
    return ExpansionTile(
      title: Text('Заказ на сумму $sum руб.'),
      children: <Widget>[...container],
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
          child: Column(
            children: <Widget> [
              Center(
                  child: Text('Личный кабинет', style: TextStyle(fontSize: 18),)
              ),
              StreamBuilder(
                stream: FirebaseFirestore.instance.collection("users-form-data").doc(FirebaseAuth.instance.currentUser!.email).snapshots(),
                builder: (BuildContext context, AsyncSnapshot snapshot){
                  var data = snapshot.data;
                  if(data==null){
                    return Center(child: CircularProgressIndicator(),);
                  }
                  return setDataToTextField(data);
                },

              ),
              Center(
                  child: Text('Заказы', style: TextStyle(fontSize: 18),)
              ),
              SizedBox(
                height: 15,
              ),
              Expanded(
                  child: SingleChildScrollView(
                    child: StreamBuilder(
                      stream: FirebaseFirestore.instance
                          .collection('orders')
                          .doc(FirebaseAuth.instance.currentUser!.email)
                          .collection('orders').snapshots(),
                      builder: (BuildContext context, AsyncSnapshot snapshot){
                        if (snapshot.hasData && !snapshot.hasError) {
                          var orders = snapshot.data!.docs
                              .map((DocumentSnapshot document) {
                            Map<String, dynamic> data =
                            document.data() as Map<String, dynamic>;
                            return data;
                          });
                          expansionTiles.length = 0;
                          for(var order in orders){
                            expansionTiles.add(populateExpansionPanel(order));
                          }
                          return Column(
                            children: [ ...expansionTiles ],
                          );
                        } else
                          return Center(child: CircularProgressIndicator(),);
                      },
                    ),
                  )
              )
            ],
          )
      ),
    );
  }
}