import 'package:flutter_test/flutter_test.dart';
import 'package:weet/main.dart';

void main() {
  testWidgets('Weet home screen renders', (WidgetTester tester) async {
    await tester.pumpWidget(const WeetApp());

    expect(find.text('Weet Relationship Map'), findsOneWidget);
    expect(find.text('Me'), findsOneWidget);
    expect(find.text('Mom'), findsOneWidget);
  });
}
