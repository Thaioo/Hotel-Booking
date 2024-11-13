var token = localStorage.getItem('jwt');

$(document).ready(function () {

    //Hiển thị notification từ database
    let pageNumber = 0;
    let pageSize = 5;
    showNotifications(pageNumber, pageSize)
    $(".view__more").click(function (e) { 
        e.preventDefault();

        pageNumber += 1
        showNotifications(pageNumber, pageSize)
    });

    // Tạo websocket
    var stompClient = null;
    var socket = new SockJS('http://localhost:9999/ws');
    var stompClient = Stomp.over(socket);
    const result = timeAgo("2024-10-01T10:34:11.730929");

    //Kết nối websocket với topic /topic/notifications'
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/notifications', function (notification) {
            let notify = JSON.parse(notification.body)

            let notifications = $('#notification__content')
            let message = `				<li>
            						${getIcon(notify.type)}
            						<div class="detail">
            							<div class="title">${notify.title}</div>
            							<p class="time">${timeAgo(notify.createDate)}</p>
            							<p class="message">${notify.message}</p>
            						</div>
            					</li>`
            notifications.prepend(message);
        });
    });
});

//Hàm show notification
function showNotifications(pageNumber, pageSize) {
    $.ajax({
        type: "GET",
        contentType: "application/json; charset=utf-8",
        url: `http://localhost:9999/notifications?pageNumber=${pageNumber}&pageSize=${pageSize}`,
        headers: {
            "Authorization": "Bearer " + token  // Truyền token vào header
        },
        success: function (response) {
            let notifications = $('#notification__content')

            
            for (let i = 0; i < response.data.length; i++) {
                let notify = response.data[i]
                let message = `				<li>
                                        ${getIcon(notify.type)}
                                        <div class="detail">
                                            <div class="title">${notify.title}</div>
                                            <p class="time">${timeAgo(notify.createDate)}</p>
                                            <p class="message">${notify.message}</p>
                                        </div>
                                    </li>`
                notifications.append(message);                
            }



        },
        error: function (response) {
            alert(response.responseJSON.message)
        }
    })
}

//Hàm get icon theo notificationType
function getIcon(notificationType){
    var iconElement = ''
    if (notificationType.includes('ALERT')) {
        iconElement = `<div class="icon lh-alert">
										<i class="ri-alarm-warning-line"></i>
									</div>`
    } else if (notificationType.includes('SUCCESS')) {
        iconElement = `<div class="icon lh-success">
										<i class="ri-check-double-line"></i>
									</div>`
    } else if (notificationType.includes('WARN')) {
        iconElement = `			<div class="icon lh-warn">
										<i class="ri-error-warning-line"></i>
									</div>`
    } else {
                iconElement = `	<div class="icon lh-warn">
										<i class="ri-close-line"></i>
									</div>`
    }

    return iconElement;
}

function timeAgo(isoDateString) {
    const givenDate = new Date(isoDateString);
    const now = new Date();

    const diffInMs = now - givenDate;
    const diffInMinutes = Math.floor(diffInMs / (1000 * 60));
    const diffInHours = Math.floor(diffInMs / (1000 * 60 * 60));
    const diffInDays = Math.floor(diffInMs / (1000 * 60 * 60 * 24));
    const diffInMonths = Math.floor(diffInDays / 30);

    if (diffInMinutes < 60) {
        return `${diffInMinutes} minutes ago`;
    } else if (diffInHours < 24) {
        return `${diffInHours} hours ago`;
    } else if (diffInDays < 30) {
        return `${diffInDays} days ago`;
    } else {
        const day = givenDate.getDate().toString().padStart(2, '0');
        const month = (givenDate.getMonth() + 1).toString().padStart(2, '0');
        const year = givenDate.getFullYear();
        return `${day}/${month}/${year}`;
    }
}