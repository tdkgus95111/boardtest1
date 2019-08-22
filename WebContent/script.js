function writeSave(){
	var wform = document.writeform;
	if (!writeform.writer.value) {
		alert("작성자를 입력하시오");
		writeform.writer.focus();
		return false;
	}
	if (!wform.writer.value) {
		alert("제목을 입력하시오");
		writeform.writer.focus();
		return false;
	}
	if (!wform.writer.value) {
		alert("내용을 입력하시오");
		writeform.writer.focus();
		return false;
	}
	if (!wform.writer.value) {
		alert("비밀번호를 입력하시오");
		writeform.writer.focus();
		return false;
	}
	
}