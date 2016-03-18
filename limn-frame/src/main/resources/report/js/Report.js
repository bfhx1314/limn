    function toggle_child_visibility(element_id) {
        if (document.getElementById(element_id + '_children') != null) {            
			$("#"+ element_id + '_children').slideToggle();
        }

        if (document.getElementById(element_id + '_foldlink') != null) {            
			$("#"+ element_id + '_foldlink').toggle();
        }
        if (document.getElementById(element_id + '_unfoldlink') != null) {            
			$("#"+ element_id + '_unfoldlink').toggle();			
        }
    }

function mouseover(obj)
{	
obj.style.background= '#ECF5FF'
}

function mouseout(obj)
{	
obj.style.backgroundColor = 'white'
}