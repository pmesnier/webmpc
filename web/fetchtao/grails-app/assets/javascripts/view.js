$(document).ready(
  function ()
  {
    var triggeredMenu;

    var shouldHideTriggeredMenu;
    var shouldHideSubmenus;

    var jMenuBarDivElem;

    //  Defined here so that they can be used throughout these handlers
    triggeredMenu = null;
    shouldHideTriggeredMenu = true;
    shouldHideSubmenus = false;

    //dashboard, this thing always loads so we need to check to see if
    //this element exists, otherwise we just return before calling
    //parentnode on it
    if (!$('#ws_nav_menu')[0]) { 
        return;
    }
    jMenuBarDivElem = $($('#ws_nav_menu')[0].parentNode);

    positionMenu = function (menuElem, actuatorElem, isTopLevel) {

      var menuPosX,
          menuPosY,

          menuWidth,
          menuHeight,

          jMenuElem,
          jActuatorElem,

          val,
          menuBorderTop,
          menuBorderRight,
          menuBorderBottom,
          menuBorderLeft,

          actuatorBorderLeft,
          actuatorBorderRight,

          container,
          containerOffsets,
          containerWidth,
          containerHeight,

          scrollPosX,
          scrollPosY,

          docWidth,
          docHeight,

          actuatorGlobalOffsets,
          actuatorWidth,
          actuatorHeight,

          boxShadowVal,
          boxShadowVals,

          offsetVal,
          numVal;

      if (isTopLevel) {
        menuPosX = actuatorElem.offsetLeft;
        menuPosY = actuatorElem.offsetTop + actuatorElem.offsetHeight;
      } else {
        menuPosX = actuatorElem.offsetLeft + actuatorElem.offsetWidth;
        menuPosY = actuatorElem.offsetTop;
      }

      //  ---
 
      jActuatorElem = $(actuatorElem);

      actuatorBorderLeft =
        isNaN(val = parseInt(jActuatorElem.css('borderLeftWidth'), 10)) ?
          0 :
          val;
      actuatorBorderRight =
        isNaN(val = parseInt(jActuatorElem.css('borderRightWidth'), 10)) ?
          0 :
          val;

      actuatorGlobalOffsets = jActuatorElem.offset();
      actuatorWidth = jActuatorElem.width();
      actuatorHeight = jActuatorElem.height();

      //  ---
 
      jMenuElem = $(menuElem);

      menuWidth = jMenuElem.width();
      menuHeight = jMenuElem.height();

      menuBorderTop =
        isNaN(val = parseInt(jMenuElem.css('borderTopWidth'), 10)) ?
          0 :
          val;
      menuBorderRight =
        isNaN(val = parseInt(jMenuElem.css('borderRightWidth'), 10)) ?
          0 :
          val;
      menuBorderBottom =
        isNaN(val = parseInt(jMenuElem.css('borderBottomWidth'), 10)) ?
          0 :
          val;
      menuBorderLeft =
        isNaN(val = parseInt(jMenuElem.css('borderLeftWidth'), 10)) ?
          0 :
          val;

      //  ---
 
      container = $('.ws_nav_menu_container')[0];

      if (container == null) {
        container = document.body;
      }

      containerOffsets = $(container).offset();

      containerWidth = $(container).width();
      containerHeight = $(container).height();

      //  ---
 
      scrollPosX = $(document).scrollLeft();
      scrollPosY = $(document).scrollTop();

      docWidth = $(document.documentElement).width();
      docHeight = $(document.documentElement).height();

      if (scrollPosX > 0) {
        scrollPosY -= 20;
      }

      if (scrollPosY > 0) {
        scrollPosX -= 20;
      }

      //  ---
 
      offsetVal = 0;

      offsetVal += actuatorGlobalOffsets.left + 4;
      offsetVal -= scrollPosX;

      boxShadowVal = jMenuElem.css('box-shadow');
      boxShadowVals = /(\d+)px (\d+)px/.exec(boxShadowVal);

      if (boxShadowVals != null &&
          !isNaN(numVal = parseInt(boxShadowVals[1], 10))) {

        offsetVal += numVal;
      }

      //  ---
 
      if ((offsetVal + menuWidth) >
          (docWidth - actuatorBorderRight + menuBorderRight) ||
          (offsetVal + menuWidth) >
          (containerOffsets.left + containerWidth)) {

        menuPosX = actuatorElem.offsetLeft + actuatorElem.offsetWidth -
                   (menuWidth + menuBorderLeft + menuBorderRight);

        if (!jMenuBarDivElem.hasClass('ws_nav_menu_align_actuator-border')) {
          menuPosX -= actuatorBorderRight;
        }

        if (!jMenuBarDivElem.hasClass('ws_nav_menu_align_menu-border')) {
          menuPosX += menuBorderRight;
        }
      }
      else
      {
        if (!jMenuBarDivElem.hasClass('ws_nav_menu_align_actuator-border')) {
          menuPosX += actuatorBorderLeft;
        }

        if (!jMenuBarDivElem.hasClass('ws_nav_menu_align_menu-border')) {
          menuPosX -= menuBorderLeft;
        }
      }

      //    Fudge factor
      menuPosX -= 1;

      //  ---

      menuElem.style.left = menuPosX + 'px';
      menuElem.style.top = menuPosY + 'px';
    };

    //  NB: #ws_nav_menu is a 'ul'

    var hideTriggeredMenu = function (aMenu) {
      setTimeout(
          function () {
            if (shouldHideTriggeredMenu !== false && triggeredMenu) {
              triggeredMenu.hide();
              triggeredMenu = null;
            }
          }, 75);
    };

    var hideSubmenus = function () {
      setTimeout(
          function () {
            var submenus;

            if (shouldHideSubmenus !== false) {
              submenus = $('ul', $('#ws_nav_menu'));
              submenus.hide();
            }
          }, 50);
    };

    //  Enter trigger - show menu for that trigger and hide any previous menu
    //  Exit trigger - hide menu for that trigger
    //  Enter menu itself - make sure to not hide menu or submenus
    //  Exit menu itself - hide menu and submenus under

    $('#ws_nav_menu li > a').bind(
        'mouseover',
        function () {
          var thisMenu,
              isTopLevel,
              triggeredMenuIsParent,
              parentMenu;

          thisMenu = $('+ ul', this); 
          //  It's a trigger, but has no menu - exit
          if (thisMenu.length === 0) {
            return;
          }

          isTopLevel = $(this).is('#ws_nav_menu > li > a');

          //  Already have a triggered menu - hide it right away
          if (triggeredMenu) {

              parentMenu = $(this).closest('ul');
              triggeredMenuIsParent = (parentMenu[0] === triggeredMenu[0]);

              if (!triggeredMenuIsParent) {
                  triggeredMenu.hide();
                  triggeredMenu = null;
              }
          }

          positionMenu(thisMenu[0], this, isTopLevel);

          thisMenu.show();

          triggeredMenu = thisMenu;

          shouldHideTriggeredMenu = false;
        });

    $('#ws_nav_menu li > a').bind(
        'mouseout',
        function () {
          shouldHideTriggeredMenu = true;
          hideTriggeredMenu();
        });

    $('#ws_nav_menu li > ul').bind(
        'mouseover',
        function () {
          if (triggeredMenu && this === triggeredMenu[0]) {
            shouldHideTriggeredMenu = false;
          }

          shouldHideSubmenus = false;
        });

    $('#ws_nav_menu li > ul').bind(
        'mouseout',
        function () {
          shouldHideTriggeredMenu = true;
          hideTriggeredMenu();

          shouldHideSubmenus = true;
          hideSubmenus();
        });
  });
