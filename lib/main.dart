import 'dart:math' as math;
import 'package:flutter/material.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import 'login_page.dart';
import 'chat_page.dart'; // <-- 이 줄을 꼭 추가해야 'ChatPage'를 찾을 수 있어요!

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  // [중요] 여기에 본인의 Supabase URL과 Anon Key를 넣으세요!
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
      title: 'Weet',
      theme: ThemeData(
        useMaterial3: true,
        colorSchemeSeed: const Color(0xFF5C6AC4),
      ),
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
        final session = snapshot.data?.session;
        if (session != null) {
          return const WeetHomePage();
        } else {
          return const LoginPage();
        }
      },
    );
  }
}

class Person {
  Person({required this.id, required this.name, required this.category, required this.score});
  final String id; // DB의 UUID를 저장
  String name;
  String category;
  int score;
}

class WeetHomePage extends StatefulWidget {
  const WeetHomePage({super.key});

  @override
  State<WeetHomePage> createState() => _WeetHomePageState();
}

class _WeetHomePageState extends State<WeetHomePage> {
  final supabase = Supabase.instance.client;
  final List<String> _categories = <String>['Family', 'Friends', 'Business', 'Other'];
  List<Person> _people = [];
  String _selectedCategory = 'All';

  @override
  void initState() {
    super.initState();
    _fetchPeople(); // 앱 실행 시 데이터 불러오기
  }

  // 데이터 불러오기
  Future<void> _fetchPeople() async {
    final userId = supabase.auth.currentUser?.id;
    if (userId == null) return;

    try {
      final data = await supabase
          .from('people')
          .select()
          .eq('user_id', userId)
          .order('created_at');
          
      setState(() {
        _people = (data as List).map((item) => Person(
          id: item['id'],
          name: item['name'],
          category: item['category'],
          score: item['score'],
        )).toList();
      });
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('데이터 로드 실패: $e')));
      }
    }
  }

  // 데이터 추가
  Future<void> _addPerson(String name, String category, int score) async {
    final userId = supabase.auth.currentUser?.id;
    try {
      await supabase.from('people').insert({
        'user_id': userId,
        'name': name,
        'category': category,
        'score': score,
      });
      _fetchPeople(); // 목록 갱신
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('추가 실패: $e')));
      }
    }
  }

  // 데이터 수정
  Future<void> _updatePerson(Person p) async {
    try {
      await supabase.from('people').update({
        'name': p.name,
        'category': p.category,
        'score': p.score,
      }).eq('id', p.id);
      _fetchPeople();
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('수정 실패: $e')));
      }
    }
  }

  // 데이터 삭제
  Future<void> _deletePerson(String id) async {
    try {
      await supabase.from('people').delete().eq('id', id);
      _fetchPeople();
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('삭제 실패: $e')));
      }
    }
  }

  List<Person> get _visiblePeople {
    if (_selectedCategory == 'All') return _people;
    return _people.where((p) => p.category == _selectedCategory).toList();
  }

  Future<void> _openAddPersonDialog() async {
    final nameController = TextEditingController();
    String category = _categories.first;
    int score = 50;

    await showDialog(
      context: context,
      builder: (context) => StatefulBuilder(
        builder: (context, setDialogState) => AlertDialog(
          title: const Text('Add Person'),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextField(controller: nameController, decoration: const InputDecoration(labelText: 'Name')),
              DropdownButtonFormField<String>(
                value: category,
                items: _categories.map((c) => DropdownMenuItem(value: c, child: Text(c))).toList(),
                onChanged: (v) => setDialogState(() => category = v!),
              ),
              Slider(min: 0, max: 100, value: score.toDouble(), onChanged: (v) => setDialogState(() => score = v.round())),
              Text('Score: $score'),
            ],
          ),
          actions: [
            TextButton(onPressed: () => Navigator.pop(context), child: const Text('Cancel')),
            FilledButton(onPressed: () {
              if (nameController.text.isNotEmpty) {
                _addPerson(nameController.text, category, score);
                Navigator.pop(context);
              }
            }, child: const Text('Save')),
          ],
        ),
      ),
    );
  }

  Future<void> _openEditPersonDialog(Person person) async {
    final nameController = TextEditingController(text: person.name);
    String category = person.category;
    int score = person.score;

    await showDialog(
      context: context,
      builder: (context) => StatefulBuilder(
        builder: (context, setDialogState) => AlertDialog(
          title: const Text('Edit Person'),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextField(controller: nameController, decoration: const InputDecoration(labelText: 'Name')),
              DropdownButtonFormField<String>(
                value: category,
                items: _categories.map((c) => DropdownMenuItem(value: c, child: Text(c))).toList(),
                onChanged: (v) => setDialogState(() => category = v!),
              ),
              Slider(min: 0, max: 100, value: score.toDouble(), onChanged: (v) => setDialogState(() => score = v.round())),
              Text('Score: $score'),
            ],
          ),
          actions: [
            TextButton(onPressed: () => Navigator.pop(context), child: const Text('Cancel')),
            FilledButton(onPressed: () {
              person.name = nameController.text;
              person.category = category;
              person.score = score;
              _updatePerson(person);
              Navigator.pop(context);
            }, child: const Text('Update')),
          ],
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
   // WeetHomePage의 build 함수 내 AppBar 부분 수정
appBar: AppBar(
  title: const Text('Weet Map'),
  leading: IconButton(
    icon: const Icon(Icons.logout),
    onPressed: () => supabase.auth.signOut(),
  ),
  actions: [
    // 채팅 페이지 이동 버튼 추가!
    IconButton(
      icon: const Icon(Icons.chat_bubble_outline),
      onPressed: () => Navigator.push(context, MaterialPageRoute(builder: (context) => const ChatPage())),
    ),
    IconButton(onPressed: _openAddPersonDialog, icon: const Icon(Icons.person_add_alt_1)),
  ],
),
      body: Column(
        children: [
          _CategoryFilterBar(
            categories: ['All', ..._categories],
            selectedCategory: _selectedCategory,
            onSelect: (v) => setState(() => _selectedCategory = v),
          ),
          Expanded(
            child: RelationshipMap(
              people: _visiblePeople,
              onLongPress: (person) {
                showModalBottomSheet(
                  context: context,
                  builder: (context) => Wrap(
                    children: [
                      ListTile(leading: const Icon(Icons.edit), title: const Text('Edit'), onTap: () { Navigator.pop(context); _openEditPersonDialog(person); }),
                      ListTile(leading: const Icon(Icons.delete, color: Colors.red), title: const Text('Delete'), onTap: () { Navigator.pop(context); _deletePerson(person.id); }),
                    ],
                  ),
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}

class RelationshipMap extends StatelessWidget {
  const RelationshipMap({super.key, required this.people, required this.onLongPress});
  final List<Person> people;
  final Function(Person) onLongPress;

  @override
  Widget build(BuildContext context) {
    return LayoutBuilder(
      builder: (context, constraints) {
        final double size = math.min(constraints.maxWidth, constraints.maxHeight);
        final Offset center = Offset(size / 2, size / 2);
        final double maxRadius = size / 2 - 40;

        return Center(
          child: SizedBox(
            width: size, height: size,
            child: Stack(
              children: [
                CustomPaint(
                  size: Size(size, size),
                  painter: _ConnectionPainter(people: people, center: center, maxRadius: maxRadius),
                ),
                Positioned(left: center.dx - 26, top: center.dy - 26, child: _CenterNode()),
                ...people.asMap().entries.map((entry) {
                  final int index = entry.key;
                  final Person person = entry.value;
                  final double angle = (2 * math.pi / math.max(1, people.length)) * index;
                  
                  final double radiusFactor = (100 - person.score) / 100;
                  final double radius = (radiusFactor * (maxRadius - 60)) + 60;

                  final double x = center.dx + math.cos(angle) * radius;
                  final double y = center.dy + math.sin(angle) * radius;

                  return Positioned(
                    left: x - 35, top: y - 35,
                    child: GestureDetector(
                      onLongPress: () => onLongPress(person),
                      child: _PersonCircle(person: person),
                    ),
                  );
                }),
              ],
            ),
          ),
        );
      },
    );
  }
}

class _ConnectionPainter extends CustomPainter {
  _ConnectionPainter({required this.people, required this.center, required this.maxRadius});
  final List<Person> people;
  final Offset center;
  final double maxRadius;

  @override
  void paint(Canvas canvas, Size size) {
    final paint = Paint()..color = Colors.black12..strokeWidth = 1.5;
    for (int i = 0; i < people.length; i++) {
      final double angle = (2 * math.pi / math.max(1, people.length)) * i;
      final double radiusFactor = (100 - people[i].score) / 100;
      final double radius = (radiusFactor * (maxRadius - 60)) + 60;
      final Offset target = Offset(center.dx + math.cos(angle) * radius, center.dy + math.sin(angle) * radius);
      canvas.drawLine(center, target, paint);
    }
  }
  @override bool shouldRepaint(covariant CustomPainter oldDelegate) => true;
}

class _PersonCircle extends StatelessWidget {
  const _PersonCircle({required this.person});
  final Person person;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: 70, height: 70,
      decoration: BoxDecoration(
        shape: BoxShape.circle,
        color: Colors.white,
        boxShadow: [
          BoxShadow(color: Colors.black.withOpacity(0.1), blurRadius: 4, offset: const Offset(0, 2))
        ],
        border: Border.all(color: Colors.blueAccent.withOpacity(0.3), width: 2),
      ),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 4),
            child: Text(person.name, style: const TextStyle(fontSize: 11, fontWeight: FontWeight.bold), overflow: TextOverflow.ellipsis),
          ),
          Text('${person.score}', style: const TextStyle(fontSize: 10, color: Colors.blueAccent)),
        ],
      ),
    );
  }
}

class _CenterNode extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      width: 52, height: 52,
      decoration: const BoxDecoration(shape: BoxShape.circle, color: Color(0xFF5C6AC4)),
      alignment: Alignment.center,
      child: const Text('Me', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
    );
  }
}

class _CategoryFilterBar extends StatelessWidget {
  const _CategoryFilterBar({required this.categories, required this.selectedCategory, required this.onSelect});
  final List<String> categories;
  final String selectedCategory;
  final ValueChanged<String> onSelect;
  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8.0),
      child: SizedBox(
        height: 44,
        child: ListView.separated(
          padding: const EdgeInsets.symmetric(horizontal: 16),
          scrollDirection: Axis.horizontal,
          itemBuilder: (context, index) => ChoiceChip(
            label: Text(categories[index]),
            selected: selectedCategory == categories[index],
            onSelected: (_) => onSelect(categories[index]),
          ),
          separatorBuilder: (_, __) => const SizedBox(width: 8),
          itemCount: categories.length,
        ),
      ),
    );
  }
}