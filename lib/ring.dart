import 'package:alarm/alarm.dart';
import 'package:flutter/material.dart';
import 'package:simple_ripple_animation/simple_ripple_animation.dart';

class ExampleAlarmRingScreen extends StatefulWidget {
  final AlarmSettings alarmSettings;
  const ExampleAlarmRingScreen({super.key, required this.alarmSettings});
  @override
  ExampleAlarmRingScreenState createState() => ExampleAlarmRingScreenState();
}

class ExampleAlarmRingScreenState extends State<ExampleAlarmRingScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.spaceAround,
          children: [
            Text(
              "Your alarm is ringing...",
              style: Theme.of(context).textTheme.titleLarge,
            ),
            const RippleAnimation(
              color: Colors.black,
              delay: Duration(milliseconds: 300),
              repeat: true,
              minRadius: 75,
              ripplesCount: 6,
              duration: Duration(milliseconds: 6 * 300),
              child: Text("🔔", style: TextStyle(fontSize: 50)),
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [
                RawMaterialButton(
                  onPressed: () {
                    final now = DateTime.now();
                    Alarm.set(
                      alarmSettings: widget.alarmSettings.copyWith(
                        dateTime: DateTime(
                          now.year,
                          now.month,
                          now.day,
                          now.hour,
                          now.minute,
                          0,
                          0,
                        ).add(const Duration(minutes: 1)),
                      ),
                    ).then((_) {
                      Alarm.stop(widget.alarmSettings.id);
                      Navigator.pop(context);
                    });
                  },
                  child: Text(
                    "Snooze",
                    style: Theme.of(context).textTheme.titleLarge,
                  ),
                ),
                RawMaterialButton(
                  onPressed: () {
                    Alarm.stop(widget.alarmSettings.id).then((_) {
                      // Update the alarm to be inactive instead of removing it
                      Alarm.set(
                          alarmSettings: widget.alarmSettings.copyWith(
                        dateTime: DateTime(
                            2050), // Set to a far future date to indicate inactivity
                      ));
                      Navigator.pop(context);
                    });
                  },
                  child: Text(
                    "Stop",
                    style: Theme.of(context).textTheme.titleLarge,
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
