from zipfile import ZipFile, ZIP_DEFLATED
from pathlib import Path

# Đường dẫn tới thư mục dự án
project_dir = Path(r'd:\aCNJAVA\lab05-minishop-swing-jdbc')
out_file = project_dir / 'BaoCaoNgan.docx'

# Đảm bảo thư mục tồn tại
project_dir.mkdir(parents=True, exist_ok=True)

# Cấu trúc nội dung báo cáo dạng (Loại dòng, Nội dung)
# Types: 'title', 'heading', 'bullet', 'text', 'blank'
data = [
    ('title', 'BÁO CÁO NGẮN - LAB 05: MiniShop Swing + JDBC'),
    ('blank', ''),
    ('heading', '1. Mục tiêu'),
    ('bullet', 'Xây dựng ứng dụng cửa hàng mini bằng Java Swing.'),
    ('bullet', 'Quản lý sản phẩm, khách hàng, hóa đơn và thống kê cơ bản.'),
    ('bullet', 'Kết nối với MySQL bằng JDBC để lưu trữ dữ liệu.'),
    ('blank', ''),
    ('heading', '2. Công nghệ sử dụng'),
    ('bullet', 'Java 17'),
    ('bullet', 'Swing GUI'),
    ('bullet', 'JDBC + MySQL'),
    ('bullet', 'Maven'),
    ('blank', ''),
    ('heading', '3. Chức năng chính'),
    ('bullet', 'Thêm, sửa, xóa sản phẩm.'),
    ('bullet', 'Thêm, sửa, xóa khách hàng.'),
    ('bullet', 'Lập hóa đơn và tính tổng tiền.'),
    ('bullet', 'Kiểm tra tồn kho trước khi bán.'),
    ('bullet', 'Giảm số lượng sản phẩm sau khi lưu hóa đơn.'),
    ('blank', ''),
    ('heading', '4. Kết quả thực hiện'),
    ('bullet', 'Ứng dụng được kết nối MySQL và chạy trên Java Swing.'),
    ('bullet', 'Database minishop_db đã được tạo và cấu hình kết nối.'),
    ('bullet', 'Logic bán hàng đã được sửa để không cho mua vượt tồn kho.'),
    ('bullet', 'Số lượng sản phẩm được giảm sau khi lưu hóa đơn.'),
    ('bullet', 'Đã kiểm tra bằng Maven test và không còn lỗi logic chính.'),
    ('blank', ''),
    ('heading', '5. Kết luận'),
    ('bullet', 'Dự án hoàn thành đúng mục tiêu học tập về Swing + JDBC.'),
    ('bullet', 'Ứng dụng phù hợp để demo và có thể nâng cấp thêm tính năng.'),
    ('blank', ''),
    ('text_right', 'Ngày báo cáo: 12/08/2026'),
]

# Hàm escape các ký tự đặc biệt trong XML
def xml_escape(text):
    return text.replace('&', '&amp;').replace('<', '&lt;').replace('>', '&gt;')

# Tạo các paragraph Word XML với định dạng chuẩn
p_xml_list = []
for p_type, text in data:
    escaped = xml_escape(text)
    
    if p_type == 'title':
        p = f'''<w:p>
            <w:pPr>
                <w:jc w:val="center"/>
                <w:spacing w:after="200"/>
            </w:pPr>
            <w:r>
                <w:rPr>
                    <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                    <w:b/>
                    <w:sz w:val="32"/>
                </w:rPr>
                <w:t xml:space="preserve">{escaped}</w:t>
            </w:r>
        </w:p>'''
    elif p_type == 'heading':
        p = f'''<w:p>
            <w:pPr>
                <w:spacing w:before="120" w:after="60"/>
            </w:pPr>
            <w:r>
                <w:rPr>
                    <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                    <w:b/>
                    <w:sz w:val="26"/>
                </w:rPr>
                <w:t xml:space="preserve">{escaped}</w:t>
            </w:r>
        </w:p>'''
    elif p_type == 'bullet':
        p = f'''<w:p>
            <w:pPr>
                <w:ind w:left="360"/>
                <w:spacing w:after="40"/>
            </w:pPr>
            <w:r>
                <w:rPr>
                    <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                    <w:sz w:val="24"/>
                </w:rPr>
                <w:t xml:space="preserve">• {escaped}</w:t>
            </w:r>
        </w:p>'''
    elif p_type == 'text_right':
        p = f'''<w:p>
            <w:pPr>
                <w:jc w:val="right"/>
                <w:spacing w:before="200"/>
            </w:pPr>
            <w:r>
                <w:rPr>
                    <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                    <w:i/>
                    <w:sz w:val="24"/>
                </w:rPr>
                <w:t xml:space="preserve">{escaped}</w:t>
            </w:r>
        </w:p>'''
    else: # blank / normal text
        p = f'''<w:p>
            <w:r>
                <w:rPr>
                    <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                    <w:sz w:val="24"/>
                </w:rPr>
                <w:t xml:space="preserve">{escaped}</w:t>
            </w:r>
        </w:p>'''
    p_xml_list.append(p)

body = ''.join(p_xml_list)

content_types = '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
  <Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>
  <Default Extension="xml" ContentType="application/xml"/>
  <Override PartName="/word/document.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml"/>
</Types>'''

rels = '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
  <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="word/document.xml"/>
</Relationships>'''

document_xml = f'''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<w:document xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships">
  <w:body>
    {body}
    <w:sectPr>
      <w:pgSz w:w="12240" w:h="15840"/>
      <w:pgMar w:top="1440" w:right="1440" w:bottom="1440" w:left="1440" w:header="720" w:footer="720" w:gutter="0"/>
    </w:sectPr>
  </w:body>
</w:document>'''

# Ghi ra file docx
with ZipFile(out_file, 'w', ZIP_DEFLATED) as z:
    z.writestr('[Content_Types].xml', content_types)
    z.writestr('_rels/.rels', rels)
    z.writestr('word/document.xml', document_xml)

print(f'Created successfully: {out_file}')