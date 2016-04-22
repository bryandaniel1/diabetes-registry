function calculatebmi()
{
    var weight = parseFloat(document.getElementById("weight").value);
    var feet = parseFloat(document.getElementById("heightFeet").value);
    var inches = parseFloat(document.getElementById("heightInches").value);
    if ((weight) && (feet)) {
        if ((inches.length === 0) || (inches === 0)) {
            inches = 0;
        }
        var totalInches = (feet * 12) + inches;
        var bmi = ((weight * 703) / (totalInches * totalInches)).toFixed(2);
        document.getElementById("bmi").value = bmi;
        if (bmi > 25) {
            var reductionGoal = (weight * 0.07).toFixed(2);
            document.getElementById("weightreduction").value
                    = reductionGoal;
        }
    }
}

function selectAll(master) {
    checkboxes = document.getElementsByName('emailList');
    for (var i = 0, n = checkboxes.length; i < n; i++) {
        if (!checkboxes[i].disabled) {
            checkboxes[i].checked = master.checked;
        }
    }
}


