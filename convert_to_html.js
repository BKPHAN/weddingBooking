const fs = require('fs');
const path = require('path');

const inputFile = path.join('C:\\Users\\BKPhan\\.gemini\\antigravity\\brain\\33626375-6149-4343-b442-00026a6b25ec', 'BAO_CAO_DO_AN.md');
const outputFile = path.join('C:\\Users\\BKPhan\\.gemini\\antigravity\\brain\\33626375-6149-4343-b442-00026a6b25ec', 'BAO_CAO_DO_AN.html');

try {
    let md = fs.readFileSync(inputFile, 'utf8');

    // 1. Convert Mermaid blocks FIRST to protect them
    md = md.replace(/```mermaid\s+([\s\S]*?)\s+```/g, '<div class="mermaid">$1</div>');

    // 2. Protect Code blocks (non-mermaid)
    md = md.replace(/```([\s\S]*?)```/g, '<pre><code>$1</code></pre>');

    // 3. Headers
    md = md.replace(/^# (.*$)/gm, '<h1 class="mt-5 mb-4 text-primary">$1</h1>');
    md = md.replace(/^## (.*$)/gm, '<h2 class="mt-4 mb-3 text-secondary">$1</h2>');
    md = md.replace(/^### (.*$)/gm, '<h3 class="mt-3 mb-2">$1</h3>');
    md = md.replace(/^#### (.*$)/gm, '<h4 class="mt-3 mb-2 text-muted">$1</h4>');

    // 4. Bold / Italic
    md = md.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
    md = md.replace(/\*(.*?)\*/g, '<em>$1</em>');

    // 5. Lists (Simple implementation: * item -> <li>item</li>)
    // We wrap strictly adjacent <li> items in <ul> later or just let them be styled list-less? 
    // Better to do a regex replace for * lines.
    md = md.replace(/^\* (.*$)/gm, '<li>$1</li>');
    
    // Wrap consecutive <li> in <ul> (Basic heuristic)
    // This is hard with regex alone, so we will use a naive approach:
    // Just replace the whole group.
    
    // 6. Paragraphs (lines that are not tags)
    // md = md.replace(/^(?!<h|<li|<div|<pre)(.*$)/gm, '<p>$1</p>'); // Too risky

    // 7. Tables (Markdown tables to HTML)
    // | A | B |
    // |---|---|
    // | C | D |
    
    // Naive table converter:
    // Header row
    // md = md.replace(/\|(.*)\|\n\|[-:| ]+\|\n/g, '<table class="table table-bordered"><thead><tr><th>$1</th></tr></thead><tbody>'); 
    // This is getting complex for regex.
    // Given the student needs a "Printable" file, preserving layout is key.
    
    // SIMPLE APPROACH: Wrap the whole thing in a <pre> tag? No, user wants pretty.
    // I will use a library-free markdown parser snippet if possible, but for now, 
    // I will stick to the manual replacements above.
    // For lists, I'll just leave them as <li> and wrap the whole content body in <ul>? No.
    // I will treat double newlines as paragraph breaks.
    
    md = md.replace(/\n\n/g, '<br><br>');

    const htmlContent = `
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Báo Cáo Đồ Án</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Mermaid JS -->
    <script src="https://cdn.jsdelivr.net/npm/mermaid/dist/mermaid.min.js"></script>
    <style>
        body { font-family: 'Times New Roman', serif; line-height: 1.6; padding: 40px; }
        h1, h2, h3 { font-family: 'Arial', sans-serif; font-weight: bold; }
        .mermaid { margin: 20px 0; text-align: center; }
        li { margin-bottom: 5px; margin-left: 20px; }
        code { background-color: #f8f9fa; padding: 2px 4px; border-radius: 4px; }
        pre { background-color: #f8f9fa; padding: 15px; border-radius: 5px; }
        @media print {
            .no-print { display: none; }
            a { text-decoration: none; color: black; }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="text-center mb-5">
            <button onclick="window.print()" class="btn btn-primary no-print btn-lg">🖨️ Lưu thành PDF / In báo cáo</button>
        </div>
        
        ${md}
        
    </div>

    <script>
        mermaid.initialize({ startOnLoad: true, theme: 'default' });
    </script>
</body>
</html>
    `;

    fs.writeFileSync(outputFile, htmlContent);
    console.log('HTML file created successfully at ' + outputFile);

} catch (err) {
    console.error('Error:', err);
}
