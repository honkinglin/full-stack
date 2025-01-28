# Online Education Platform  

An open-source, modular online education platform providing a complete solution for both frontend user features and backend administrative management.

---

### Features  

#### **Frontend Modules**  
- **Authentication & User Management**:  
  - Mobile dynamic code login/registration  
  - Global search functionality  
  - User profile center  

- **Course Features**:  
  - Course listing and management  
  - Instructor and institution management  
  - Video playback  
  - Recommendations for popular and related courses  

- **User Interaction**:  
  - Course reviews and ratings  
  - User favorites  

#### **Backend Management System**  
- **Course Management**:  
  - Course resources  
  - Review moderation  
  - Rotational course display  

- **Authentication & Permissions**:  
  - Role-based user management  
  - Access control and permission assignment  
  - User activity logs  

- **Institution Management**:  
  - Course institutions  
  - Instructor carousel images  

---

### Tech Stack  

- **Backend**: Python 3, Django 5
- **Frontend**: HTML5, CSS3, JavaScript (with modern frameworks if needed)  
- **Database**: MySQL  

---

### Installation  

1. Clone the repository:  
   ```bash  
   git https://github.com/honkinglin/OnlineEducation.git
   cd OnlineEducation
   ```  

2. Install dependencies:  
   ```bash  
   pip install -r requirements.txt  
   ```  

3. Configure your MySQL database in `settings.py`:  
   ```python  
   DATABASES = {  
       'default': {  
           'ENGINE': 'django.db.backends.mysql',  
           'NAME': 'your_database_name',  
           'USER': 'your_user',  
           'PASSWORD': 'your_password',  
           'HOST': 'localhost',  
           'PORT': '3306',  
       }  
   }  
   ```  

4. Run database migrations:  
   ```bash  
   python manage.py migrate  
   ```  

5. Start the server:  
   ```bash  
   python manage.py runserver  
   ```  

6. Access the platform at `http://127.0.0.1:8000`.  

---

### Directory Structure  

```plaintext  
OnlineEducation/  
├── manage.py                # Entry point for managing the Django project  
├── requirements.txt         # List of project dependencies  
├── README.md                # Project documentation  
├── apps/                    # Directory for Django applications  
│   ├── users/               # User management module  
│   │   ├── migrations/      # Database migration files  
│   │   │   ├── __init__.py  # Marks this directory as a Python package  
│   │   ├── templates/       # Templates related to user management  
│   │   │   └── users/  
│   │   │       └── login.html  # Template for user login page  
│   │   ├── admin.py         # Admin panel configuration for the app  
│   │   ├── apps.py          # App-specific configuration file  
│   │   ├── models.py        # Database models for the app  
│   │   ├── views.py         # Views (controllers) for user-related operations  
│   │   ├── forms.py         # Form definitions for user-related data  
│   │   ├── serializers.py   # Serialization logic for APIs  
│   │   └── urls.py          # URL routing for user module  
│   ├── courses/             # Course management module  
│   │   ├── migrations/  
│   │   ├── templates/  
│   │   ├── admin.py  
│   │   ├── apps.py  
│   │   ├── models.py  
│   │   ├── views.py  
│   │   ├── serializers.py  
│   │   └── urls.py  
│   ├── institutions/        # Institution management module  
│   │   ├── migrations/  
│   │   ├── templates/  
│   │   ├── admin.py  
│   │   ├── apps.py  
│   │   ├── models.py  
│   │   ├── views.py  
│   │   ├── serializers.py  
│   │   └── urls.py  
├── static/                  # Static files directory  
│   ├── css/                 # CSS files for styling  
│   ├── js/                  # JavaScript files for frontend behavior  
│   └── images/              # Image assets  
├── templates/               # Global templates directory  
│   └── base.html            # Base template for extending other templates  
└── docs/                    # Documentation directory  
    ├── api/                 # API documentation files  
    └── architecture/        # Architecture-related documents  
```  

---

### Key Features  

1. **Scalable Architecture**: Designed for flexibility and easy integration of new modules.  
2. **User-Friendly Interface**: Mobile-friendly, intuitive design for learners and administrators.  
3. **Comprehensive Analytics**: Backend dashboard with detailed course and user activity reports.  

---

### Contribution  

Contributions are welcome!  
1. Fork the repository.  
2. Create a feature branch: `git checkout -b feature-name`.  
3. Commit your changes: `git commit -m "Add feature-name"`.  
4. Push to the branch: `git push origin feature-name`.  
5. Open a pull request.  

---

### License  

This project is licensed under the MIT License. See the `LICENSE` file for details.  
