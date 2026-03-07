import 'dart:io';
import 'package:flutter/material.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import 'package:image_picker/image_picker.dart';
import 'package:flutter_image_compress/flutter_image_compress.dart';
import 'package:path_provider/path_provider.dart';

class ChatPage extends StatefulWidget {
  const ChatPage({super.key});

  @override
  State<ChatPage> createState() => _ChatPageState();
}

class _ChatPageState extends State<ChatPage> {
  final _supabase = Supabase.instance.client;
  final _controller = TextEditingController();
  final _picker = ImagePicker();
  
  // 실시간 데이터를 담을 스트림
  late final Stream<List<Map<String, dynamic>>> _messageStream;

  @override
  void initState() {
    super.initState();
    // 1. 여기서 테이블을 실시간으로 구독합니다. (중요!)
    _messageStream = _supabase
        .from('chat_messages')
        .stream(primaryKey: ['id'])
        .order('created_at', ascending: false); // 최신 메시지가 위로 오도록
  }

  // 메시지 보내기 (150자 제한)
  Future<void> _sendMessage({String? content, String? imageUrl}) async {
    if ((content == null || content.isEmpty) && imageUrl == null) return;
    
    final user = _supabase.auth.currentUser;
    try {
      await _supabase.from('chat_messages').insert({
        'sender_id': user!.id,
        'sender_name': user.userMetadata?['username'] ?? '익명',
        'content': content,
        'image_url': imageUrl,
      });
      _controller.clear();
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('전송 에러: $e')));
    }
  }

  // 이미지 전송 로직 (1MB 제한)
  Future<void> _pickAndUploadImage() async {
    final XFile? image = await _picker.pickImage(source: ImageSource.gallery);
    if (image == null) return;

    final dir = await getTemporaryDirectory();
    final targetPath = '${dir.absolute.path}/temp_${DateTime.now().millisecondsSinceEpoch}.jpg';
    
    var result = await FlutterImageCompress.compressAndGetFile(
      image.path, targetPath, quality: 60, minWidth: 800, minHeight: 800,
    );

    if (result == null) return;
    final file = File(result.path);

    if (await file.length() > 1024 * 1024) {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('이미지가 1MB를 넘습니다.')));
      return;
    }

    final fileName = 'public/${DateTime.now().millisecondsSinceEpoch}.jpg';
    await _supabase.storage.from('chat_images').upload(fileName, file);
    final imageUrl = _supabase.storage.from('chat_images').getPublicUrl(fileName);

    _sendMessage(imageUrl: imageUrl);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('가족 채팅')),
      body: Column(
        children: [
          Expanded(
            child: StreamBuilder<List<Map<String, dynamic>>>(
              stream: _messageStream,
              builder: (context, snapshot) {
                if (snapshot.hasError) return Center(child: Text('에러 발생: ${snapshot.error}'));
                if (!snapshot.hasData) return const Center(child: CircularProgressIndicator());
                
                final messages = snapshot.data!;
                return ListView.builder(
                  reverse: true, // 입력창 바로 위가 최신 메시지
                  itemCount: messages.length,
                  itemBuilder: (context, index) {
                    final msg = messages[index];
                    final isMe = msg['sender_id'] == _supabase.auth.currentUser?.id;
                    
                    return Container(
                      padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 5),
                      alignment: isMe ? Alignment.centerRight : Alignment.centerLeft,
                      child: Column(
                        crossAxisAlignment: isMe ? CrossAxisAlignment.end : CrossAxisAlignment.start,
                        children: [
                          Text(msg['sender_name'], style: const TextStyle(fontSize: 10, color: Colors.grey)),
                          const SizedBox(height: 4),
                          if (msg['image_url'] != null) 
                            Padding(
                              padding: const EdgeInsets.only(bottom: 5),
                              child: ClipRRect(
                                borderRadius: BorderRadius.circular(8),
                                child: Image.network(msg['image_url'], width: 200, fit: BoxFit.cover),
                              ),
                            ),
                          if (msg['content'] != null && msg['content'].toString().isNotEmpty)
                            Container(
                              padding: const EdgeInsets.all(10),
                              decoration: BoxDecoration(
                                color: isMe ? const Color(0xFF5C6AC4) : Colors.grey[300],
                                borderRadius: BorderRadius.circular(10),
                              ),
                              child: Text(
                                msg['content'],
                                style: TextStyle(color: isMe ? Colors.white : Colors.black),
                              ),
                            ),
                        ],
                      ),
                    );
                  },
                );
              },
            ),
          ),
          // 입력창 영역
          Container(
            padding: const EdgeInsets.all(8),
            decoration: BoxDecoration(color: Colors.white, border: Border(top: BorderSide(color: Colors.grey[200]!))),
            child: Row(
              children: [
                IconButton(icon: const Icon(Icons.image, color: Color(0xFF5C6AC4)), onPressed: _pickAndUploadImage),
                Expanded(
                  child: TextField(
                    controller: _controller,
                    maxLength: 150,
                    decoration: const InputDecoration(hintText: '메시지 입력...', counterText: "", border: InputBorder.none),
                  ),
                ),
                IconButton(icon: const Icon(Icons.send, color: Color(0xFF5C6AC4)), onPressed: () => _sendMessage(content: _controller.text)),
              ],
            ),
          ),
        ],
      ),
    );
  }
}