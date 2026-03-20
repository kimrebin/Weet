import 'dart:math' as math;
import 'package:flutter/material.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import 'package:qr_flutter/qr_flutter.dart';
import 'package:mobile_scanner/mobile_scanner.dart';
import 'login_page.dart';
import 'chat_page.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Supabase.initialize(
    url: 'https://nsvunavsdjegjppsqopw.supabase.co',
    anonKey: 'sb_publishable_PXSlvkjX62rOYAkyv9L2Kw_Yxm2MNH-',
  );
  runApp(const WeetApp());
}

class WeetApp extends StatelessWidget {
  const WeetApp({super.key});
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      theme: ThemeData(useMaterial3: true, colorSchemeSeed: const Color(0xFF5C6AC4)),
      home: const AuthCheck(),
    );
  }
}

class AuthCheck extends StatelessWidget {
  const AuthCheck({super.key});
  @override
  Widget build(BuildContext context) {
    return StreamBuilder<AuthState>(
      stream: Supabase.instance.client.auth.onAuthStateChange,
      builder: (context, snapshot) {
        if (snapshot.data?.session != null) return const WeetHomePage();
        return const LoginPage();
      },
    );
  }
}

class Person {
  Person({required this.id, required this.name, required this.category, required this.score, this.relatedPersonId, this.uid});
  final String id;
  String name;
  String category;
  int score;
  String? relatedPersonId;
  String? uid; // 친구의 실제 계정 UID (채팅용)
}

class WeetHomePage extends StatefulWidget {
  const WeetHomePage({super.key});
  @override
  State<WeetHomePage> createState() => _WeetHomePageState();
}

class _WeetHomePageState extends State<WeetHomePage> {
  final supabase = Supabase.instance.client;
  List<Person> _people = [];

  @override
  void initState() {
    super.initState();
    _fetchPeople();
  }

  Future<void> _fetchPeople() async {
    final userId = supabase.auth.currentUser?.id;
    if (userId == null) return;
    final data = await supabase.from('people').select().eq('user_id', userId).order('created_at');
    setState(() {
      _people = (data as List).map((item) => Person(
        id: item['id'],
        name: item['name'],
        category: item['category'],
        score: item['score'],
        relatedPersonId: item['related_person_id'],
        uid: item['friend_uid'],
      )).toList();
    });
  }

  void _showMyQr() {
    final myId = supabase.auth.currentUser?.id;
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('My QR Code'),
        content: SizedBox(
          width: 200, height: 200,
          child: QrImageView(data: myId ?? '', version: QrVersions.auto, size: 200.0),
        ),
        actions: [TextButton(onPressed: () => Navigator.pop(context), child: const Text('Close'))],
      ),
    );
  }

  void _openEditDialog(Person? person) {
    final nameController = TextEditingController(text: person?.name);
    String category = person?.category ?? 'Friends';
    int score = person?.score ?? 50;
    String? selectedRelatedId = person?.relatedPersonId;
    String? friendUid = person?.uid;

    showDialog(
      context: context,
      builder: (context) => StatefulBuilder(
        builder: (context, setDialogState) => AlertDialog(
          title: Text(person == null ? 'Add Person' : 'Edit Person'),
          content: SingleChildScrollView(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                TextField(controller: nameController, decoration: const InputDecoration(labelText: 'Name')),
                DropdownButtonFormField<String>(
                  value: category,
                  items: ['Family', 'Friends', 'Business', 'Other'].map((c) => DropdownMenuItem(value: c, child: Text(c))).toList(),
                  onChanged: (v) => setDialogState(() => category = v!),
                ),
                Slider(min: 0, max: 100, value: score.toDouble(), onChanged: (v) => setDialogState(() => score = v.round())),
                const Divider(),
                Text(friendUid == null ? "No Linked Account" : "Linked: ${friendUid!.substring(0,8)}..."),                ElevatedButton.icon(
                  onPressed: () async {
                    final result = await Navigator.push(context, MaterialPageRoute(builder: (context) => const QrScannerPage()));
                    if (result != null) setDialogState(() => friendUid = result);
                  },
                  icon: const Icon(Icons.qr_code_scanner),
                  label: const Text('Scan Friend QR'),
                ),
                DropdownButtonFormField<String?>(
                  value: selectedRelatedId,
                  hint: const Text('Connect to...'),
                  items: [
                    const DropdownMenuItem(value: null, child: Text('None')),
                    ..._people.where((p) => p.id != person?.id).map((p) => DropdownMenuItem(value: p.id, child: Text(p.name)))
                  ],
                  onChanged: (v) => setDialogState(() => selectedRelatedId = v),
                ),
              ],
            ),
          ),
          actions: [
            TextButton(onPressed: () => Navigator.pop(context), child: const Text('Cancel')),
            FilledButton(onPressed: () async {
              final userId = supabase.auth.currentUser?.id;
              final data = {
                'user_id': userId,
                'name': nameController.text,
                'category': category,
                'score': score,
                'related_person_id': selectedRelatedId,
                'friend_uid': friendUid,
              };
              if (person == null) {
                await supabase.from('people').insert(data);
              } else {
                await supabase.from('people').update(data).eq('id', person.id);
              }
              _fetchPeople();
              Navigator.pop(context);
            }, child: const Text('Save')),
          ],
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Weet Network'),
        actions: [
          IconButton(icon: const Icon(Icons.settings), onPressed: () {
            showModalBottomSheet(context: context, builder: (context) => ListView(
              shrinkWrap: true,
              children: _people.map((p) => ListTile(
                title: Text(p.name),
                trailing: const Icon(Icons.edit),
                onTap: () { Navigator.pop(context); _openEditDialog(p); },
              )).toList(),
            ));
          }),
          IconButton(icon: const Icon(Icons.add), onPressed: () => _openEditDialog(null)),
        ],
      ),
      body: RelationshipMap(
        people: _people,
        onCenterTap: _showMyQr,
        onPersonTap: (p) {
          if (p.uid != null) {
             // 1:1 채팅 페이지로 이동 로직 (ChatPage 수정 필요)
             Navigator.push(context, MaterialPageRoute(builder: (context) => const ChatPage()));
          } else {
             ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('QR 스캔으로 계정을 먼저 연결해주세요!')));
          }
        },
      ),
    );
  }
}

class QrScannerPage extends StatelessWidget {
  const QrScannerPage({super.key});
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Scan QR')),
      body: MobileScanner(
        onDetect: (capture) {
          final List<Barcode> barcodes = capture.barcodes;
          if (barcodes.isNotEmpty) Navigator.pop(context, barcodes.first.rawValue);
        },
      ),
    );
  }
}

class RelationshipMap extends StatelessWidget {
  const RelationshipMap({super.key, required this.people, required this.onPersonTap, required this.onCenterTap});
  final List<Person> people;
  final Function(Person) onPersonTap;
  final VoidCallback onCenterTap;

  @override
  Widget build(BuildContext context) {
    return LayoutBuilder(builder: (context, constraints) {
      final double size = math.min(constraints.maxWidth, constraints.maxHeight);
      final Offset center = Offset(size / 2, size / 2);
      final double maxRadius = size / 2 - 40;
      Map<String, Offset> positions = {};
      for (int i = 0; i < people.length; i++) {
        final double angle = (2 * math.pi / math.max(1, people.length)) * i;
        final double radius = ((100 - people[i].score) / 100 * (maxRadius - 60)) + 60;
        positions[people[i].id] = Offset(center.dx + math.cos(angle) * radius, center.dy + math.sin(angle) * radius);
      }
      return Center(
        child: SizedBox(width: size, height: size, child: Stack(children: [
          CustomPaint(size: Size(size, size), painter: _NetworkPainter(people: people, center: center, positions: positions)),
          Positioned(left: center.dx - 26, top: center.dy - 26, child: GestureDetector(onTap: onCenterTap, child: _CenterNode())),
          ...people.map((p) => Positioned(left: positions[p.id]!.dx - 35, top: positions[p.id]!.dy - 35, 
            child: GestureDetector(onTap: () => onPersonTap(p), child: _PersonCircle(person: p)))),
        ])),
      );
    });
  }
}

class _NetworkPainter extends CustomPainter {
  _NetworkPainter({required this.people, required this.center, required this.positions});
  final List<Person> people;
  final Offset center;
  final Map<String, Offset> positions;
  @override
  void paint(Canvas canvas, Size size) {
    final pBase = Paint()..color = Colors.black12..strokeWidth = 1.0;
    final pRel = Paint()..color = Colors.blueAccent.withOpacity(0.5)..strokeWidth = 2.0;
    for (var p in people) {
      canvas.drawLine(center, positions[p.id]!, pBase);
      if (p.relatedPersonId != null && positions.containsKey(p.relatedPersonId)) {
        canvas.drawLine(positions[p.id]!, positions[p.relatedPersonId]!, pRel);
      }
    }
  }
  @override bool shouldRepaint(CustomPainter old) => true;
}

class _PersonCircle extends StatelessWidget {
  const _PersonCircle({required this.person});
  final Person person;
  @override
  Widget build(BuildContext context) {
    return Container(width: 70, height: 70, decoration: BoxDecoration(shape: BoxShape.circle, color: Colors.white, 
      boxShadow: [BoxShadow(color: Colors.black12, blurRadius: 4)],
      border: Border.all(color: person.uid != null ? Colors.blueAccent : Colors.grey.withOpacity(0.3))),
      child: Center(child: Text(person.name, textAlign: TextAlign.center, style: const TextStyle(fontSize: 10))),
    );
  }
}

class _CenterNode extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(width: 52, height: 52, decoration: const BoxDecoration(shape: BoxShape.circle, color: Color(0xFF5C6AC4)),
      child: const Center(child: Icon(Icons.qr_code, color: Colors.white, size: 24)));
  }
}