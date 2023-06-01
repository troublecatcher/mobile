import 'package:flutter/material.dart';
import 'dart:async';
import 'dart:ui';
import 'package:shared_preferences/shared_preferences.dart';

import 'package:pedometer/pedometer.dart';
import 'package:permission_handler/permission_handler.dart';

String formatDate(DateTime d) {
  return d.toString().substring(0, 19);
}

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  late Stream<StepCount> _stepCountStream;
  late Stream<PedestrianStatus> _pedestrianStatusStream;
  String _status = '?', _steps = '0';
  int _oldsteps = 0;


  @override
  void initState() {
    super.initState();

    initPlatformState();


  }

  void onStepCount(StepCount event) {
    print(event);
    setState(() {

      _steps = (event.steps-_oldsteps).toString();
    });
  }

  void onPedestrianStatusChanged(PedestrianStatus event) {
    print(event);
    setState(() {
      _status = event.status;
    });
  }

  void onPedestrianStatusError(error) {
    print('onPedestrianStatusError: $error');
    setState(() {
      _status = 'Pedestrian Status not available';
    });
    print(_status);
  }

  void onStepCountError(error) {
    print('onStepCountError: $error');
    setState(() {
      _steps = 'Step Count not available';
    });
  }



  Future<void> initPlatformState() async {

    _pedestrianStatusStream = Pedometer.pedestrianStatusStream;

    _pedestrianStatusStream
        .listen(onPedestrianStatusChanged)
        .onError(onPedestrianStatusError);

    _stepCountStream =Pedometer.stepCountStream;
    _stepCountStream.listen(onStepCount).onError(onStepCountError);

// Check if permissions are granted

    if (await Permission.activityRecognition.request().isGranted) {
      if (await Permission.sensors.request().isGranted) {
        print("Permissions granted");
      } else {
        print("Sensors permission not granted");
      }
    } else {
      print("Activity recognition permission not granted");
    }
    resetSteps();
  }
  void resetSteps() {
    setState(() {
      _oldsteps += int.parse(_steps);
      _steps = '0';
    });
  }

  @override
  Widget build(BuildContext context) {
    AssetImage image = const AssetImage('assets/22.jpg');
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Шагомер'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Image(image: image),
              Text(
                'Шагов пройдено:',
                style: TextStyle(fontSize: 20),
              ),
              Text(
                _steps,
                style: TextStyle(fontSize: 40),
              ),

              Divider(
                height: 50,
                thickness: 0,
                color: Colors.white,
              ),
              Text(
                'Статус:',
                style: TextStyle(fontSize: 20),
              ),
              Icon(
                _status == 'walking'
                    ? Icons.directions_walk
                    : _status == 'stopped'
                    ? Icons.accessibility_new
                    : Icons.error,
                size: 80,
              ),
              Center(
                child: 
                _status == 'walking' || _status == 'stopped'?
                Text(
                  _status == 'walking'? 'ходим':'стоим',style: const TextStyle(fontSize: 20),
                )
                :TextButton(
                  onPressed:(){
                  openAppSettings();
                },
                  child: const Text("Повторное получение прав", style: TextStyle(fontSize: 20, color: Colors.blueAccent))),
              )
            ],
          ),
        ),
      ),
    );
  }
}
