// READY
$(document).ready(function() {
  
  // btnReg
  $("#btnReg").click(function() {
    window.location.href = "/register"
  });
  
  $("#btnDelete").submit(function() {
    if (confirm("Are you ok?")) {
      return true;
    }
    return false;
  });
});